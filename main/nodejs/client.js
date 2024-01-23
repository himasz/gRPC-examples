const grpc = require('@grpc/grpc-js');
const loader = require('@grpc/proto-loader');
const PROTO_PATH = __dirname + '/../proto/SimpleService.proto';

const packageDefinition = loader.loadSync(
    PROTO_PATH,
    { keepCase: true, longs: String, enums: String, defaults: true, oneofs: true }
);

const packageDef = grpc.loadPackageDefinition(packageDefinition);

let service = packageDef.simpleservice;
const client = new service.SimpleService('localhost:3456', grpc.credentials.createInsecure());

const request = { message: 'ebrahim' };

client.helloV1(request, (error, response) => {
    if (!error) {
        console.log('Result:', response.message);
    } else {
        console.error('Error:', error);
    }
});
