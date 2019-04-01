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

describe('Graphql Query Tests - Query by WeakReference tests', () => {

    test('companyByIndustry query test', async () => {
       const response = await axios.post(server, {
           query:
           `{
                companyByIndustry(property: title, equals: "Media") {
                    metadata {
                        created
                        createdBy
                        lastModified
                        lastModifiedBy
                        lastPublished
                        lastPublishedBy
                        uuid
                        path
                    }
                }
            }`
       }, axiosConf);

        expect(data.data.companyByIndustry.length).toBe(3);
    });

    test('companyByIndustryConnection query test', async () => {
        const response = await axios.post(server, {
            query:
            `{
                companyByIndustryConnection(companyByIndustryArgs: {
                    preview: true, sortBy: { fieldName: "title", sortType: ASC},
                    property: title, equals: "Media"
                }){
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        nodesCount
                        totalCount
                    }
                    edges {
                        node {
                            title
                            overview
                        }
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.companyByIndustryConnection.edges[0].length).toBe(3);
        expect(data.data.companyByIndustryConnection.edges[0].node.title).not.toBeNull();
        expect(data.data.companyByIndustryConnection.edges[0].node.overview).not.toBeNull();
        expect(data.data.companyByIndustryConnection.edges[1].node.title).not.toBeNull();
        expect(data.data.companyByIndustryConnection.edges[1].node.overview).not.toBeNull();
        expect(data.data.companyByIndustryConnection.edges[2].node.title).not.toBeNull();
        expect(data.data.companyByIndustryConnection.edges[2].node.overview).not.toBeNull();
    });

});
