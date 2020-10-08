import { ChainException } from './ChainException';

export class HttpException extends ChainException {
    constructor({ message, cause, code }) {
        super({ message: message, cause: cause });
        this.code = code;
    }
}
