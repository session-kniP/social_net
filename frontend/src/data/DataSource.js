export class DataSource {

    constructor(source) {
        this.source = source;
    }

    async call(params) {
        return await this.source(params);
    }

}