# Use the official Node.js 14 image as a base
FROM node:18.19.1-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json to the container
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code to the container
COPY . .

# Build the Angular app for production
RUN npm run build --prod

# Use the official Nginx image as a base for serving Angular app
FROM nginx:alpine

# Copy the built Angular app from the build stage to the Nginx server
COPY --from=build /app/dist/* /usr/share/nginx/html/

# Expose port 80 for the Nginx server
EXPOSE 80

# Command to start the Nginx server
CMD ["nginx", "-g", "daemon off;"]
