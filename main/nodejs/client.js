const grpc = require('@grpc/grpc-js');
const loader = require('@grpc/proto-loader');
const PROTO_PATH = __dirname + '/../proto/SimpleService.proto';

const packageDefinition = loader.loadSync(
    PROTO_PATH,
    {keepCase: true, longs: String, enums: String, defaults: true, oneofs: true}
);

const packageDef = grpc.loadPackageDefinition(packageDefinition);

let service = packageDef.simpleservice;
const client = new service.SimpleService('localhost:3456', grpc.credentials.createInsecure());

const request = {message: 'ebrahim'};

//  rpc helloV1 (HelloRequest) returns (HelloResponse);
client.helloV1(request, (error, response) => {
    if (!error) {
        console.log('Response:', response.message);
    } else {
        console.error('Error:', error);
    }
});

//  rpc helloV2 (HelloRequest) returns (stream HelloResponse);
client.helloV2(request)
    .on('data', (response) => console.log('Response:', response.message))
    .on('end', () => console.log('helloV2 streaming ended'));

//  rpc helloV3 (stream HelloRequest) returns (HelloResponse);
const callV3 = client.helloV3((error, response) => {
    if (!error) {
        console.log('Response from helloV3:', response.message);
    } else {
        console.error('Error:', error);
    }
});

callV3.write({message: 'Ebrahim '});
callV3.write({message: 'Zidan'});
callV3.end();

//  rpc helloV4 (stream HelloRequest) returns (stream HelloResponse);
const callV4 = client.helloV4();

callV4.write({message: 'Ebrahim'})
callV4.write({message: 'Zidan'})
callV4.on('data', (response) => console.log('Response from helloV4:', response.message))
callV4.on('end', () => console.log('helloV4 streaming ended'))
callV4.on('error', (error) => console.error('Error from helloV4:', error));
callV4.end();
