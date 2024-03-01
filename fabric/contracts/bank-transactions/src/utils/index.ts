export function assertValue<T>(value: T | undefined | null, message: string): T {
    if (!value || typeof value === 'string' && value.length === 0) {
        throw new Error(message);
    }

    return value;
}