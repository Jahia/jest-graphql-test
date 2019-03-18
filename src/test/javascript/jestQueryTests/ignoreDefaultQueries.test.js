import axios from 'axios';

let {isFreePort} = require('node-port-check');

//server
let server = 'http://dev.org:8081/qa/modules/graphql';

if (!isFreePort(8080) || !isFreePort(8030)){
    server = 'http://localhost:8080/modules/graphql';
}

//headers config
const axiosConf = {
    headers: {
        authorization: 'Basic cm9vdDpyb290MTIzNA=='
    }
};

//TESTRAIL ID: C1566675
describe('Graphql Query Tests - Ignore Default Queries', () => {

    test('allNewsSDL query test: should retrieve an error', async () => {
        const response = await axios.post(server, {
            query:
            `{
                allNewsSDL (language:"en") {
                    title
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.errors[0]).toHaveProperty("description", "Field 'allNewsSDL' in type 'Query' is undefined")
    });

    test('newsSDLById query test: should retrieve an error', async () => {
        const response = await axios.post(server, {
            query:
                `{
                newsSDLById (id: "c81d50fd-7807-46ae-a4e1-96b5ba9eb2e5") {
                    title
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.errors[0]).toHaveProperty("description", "Field 'newsSDLById' in type 'Query' is undefined");
    });


    test('newsSDLByPath query test: should retrieve an error', async () => {
        const response = await axios.post(server, {
            query:
                `{
                newsSDLByPath (path: "/sites/digitall/home/about/history/area-main/timeline/baumquist-joins-digitall-as-cont") {
                    title
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.errors[0]).toHaveProperty("description", "Field 'newsSDLByPath' in type 'Query' is undefined");
    });


});
