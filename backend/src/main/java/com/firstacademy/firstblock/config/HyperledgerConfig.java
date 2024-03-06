package com.firstacademy.firstblock.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.client.identity.Signer;
import org.hyperledger.fabric.client.identity.Signers;
import org.hyperledger.fabric.client.identity.X509Identity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

@Configuration
public class HyperledgerConfig {
	private Environment environment;
	private String MSP_ID;
	private String CHANNEL_NAME;
	private String CHAINCODE_NAME;
	private Path CRYPTO_PATH;
	private Path CERT_PATH;
	private Path KEY_DIR_PATH;
	private Path TLS_CERT_PATH;
	private String PEER_ENDPOINT;
	private String OVERRIDE_AUTH;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public HyperledgerConfig(Environment environment) {
		this.environment = environment;
		if (isDocker()) {
			CRYPTO_PATH = Paths.get("/app/fabric/org1.example.com");
		} else {
			String rootDir = this.findRootDirectory();
			if (rootDir == null) {
				throw new Error("Pom.xml file does not exist");
			}

			CRYPTO_PATH = Paths.get(rootDir)
					.resolve("../fabric/network/organizations/peerOrganizations/org1.example.com");
		}

		MSP_ID = environment.getProperty("hyperledger.fabric.org.msp");
		CHANNEL_NAME = environment.getProperty("hyperledger.fabric.org.channel");
		CHAINCODE_NAME = environment.getProperty("hyperledger.fabric.org.cc-name");
		CERT_PATH = CRYPTO_PATH
				.resolve(Paths.get("users/User1@org1.example.com/msp/signcerts/User1@org1.example.com-cert.pem"));
		// Path to user private key directory.
		KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/keystore"));
		// Path to peer tls certificate.
		TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer0.org1.example.com/tls/ca.crt"));
		// Gateway peer end point.
		PEER_ENDPOINT = environment.getProperty("hyperledger.fabric.org.peer-endpoint");
		OVERRIDE_AUTH = environment.getProperty("hyperledger.fabric.org.override-auth");
	}

	@Bean
	public Gateway configureFabric(final String[] args) throws Exception {

		try {
			var channel = newGrpcConnection();

			var builder = Gateway.newInstance().identity(newIdentity()).signer(newSigner()).connection(channel)
					// Default timeouts for different gRPC calls
					.evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
					.endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
					.submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
					.commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));

			Gateway gateway = builder.connect();
			System.out.println("Sucessfully connected to Gateway");
			return gateway;
		} catch (Exception e) {
			System.err.println("Could not connect to gateway");
			throw new RuntimeException(e.getMessage());
		}

	}

	@Bean
	public Contract createChannel(Gateway gateway) throws Exception {
		var network = gateway.getNetwork(CHANNEL_NAME);
		return network.getContract(CHAINCODE_NAME);
	}

	private ManagedChannel newGrpcConnection() throws IOException {
		var credentials = TlsChannelCredentials.newBuilder()
				.trustManager(TLS_CERT_PATH.toFile())
				.build();
		return Grpc.newChannelBuilder(PEER_ENDPOINT, credentials)
				.overrideAuthority(OVERRIDE_AUTH)
				.build();
	}

	private Identity newIdentity() throws IOException, CertificateException {
		var certReader = Files.newBufferedReader(CERT_PATH);
		var certificate = Identities.readX509Certificate(certReader);

		return new X509Identity(MSP_ID, certificate);
	}

	private Signer newSigner() throws IOException, InvalidKeyException {
		var keyReader = Files.newBufferedReader(getPrivateKeyPath());
		var privateKey = Identities.readPrivateKey(keyReader);

		return Signers.newPrivateKeySigner(privateKey);
	}

	private Path getPrivateKeyPath() throws IOException {
		try (var keyFiles = Files.list(KEY_DIR_PATH)) {
			return keyFiles.findFirst().orElseThrow();
		}
	}

	public String findRootDirectory() {
		File currentDir = new File(System.getProperty("user.dir"));
		while (currentDir != null) {
			File pomFile = new File(currentDir, "pom.xml");
			if (pomFile.exists()) {
				return currentDir.getAbsolutePath();
			}
			currentDir = currentDir.getParentFile();
		}

		return null;
	}

	private boolean isDocker() {
		return environment.getProperty("HOSTNAME") != null;
	}
}