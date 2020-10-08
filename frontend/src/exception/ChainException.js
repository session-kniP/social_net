export class ChainException extends Error {
    constructor({ message, cause }) {
        super(`In ChainException: ${message}\n`);
        this.cause = cause;
    }
}
