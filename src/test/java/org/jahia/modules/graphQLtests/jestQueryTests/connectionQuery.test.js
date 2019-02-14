import axios from 'axios';

//server
const server = 'http://localhost:8080/modules/graphql';

//headers config
const axiosConf = {
    headers: {
        authorization: 'Basic cm9vdDpyb290MTIzNA=='
    }
};

describe('GraphQL Query Tests - by ALL connections tests', () => {

    test('Query allMyNewsConnection with no arguments', async () => {
        const response = await axios.post(server, {
           query:
           `{
                allMyNewsConnection {
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        totalCount
                    }
                    edges {
                        cursor
                        node {
                            path
                            body
                            uuid
                            subtexts {
                                text
                            }
                        }
                    }
                }
           }`
        }, axiosConf);

        const { data } = response;

        //check that pageInfo node is not null
        expect(data.data.allMyNewsConnection.pageInfo).not.toBeNull();

        //check that totalCount node is 3
        expect(data.data.allMyNewsConnection.pageInfo.totalCount).toBe(3);

        //check that the edges node has a length of 3 subnodes
        expect(data.data.allMyNewsConnection.edges.length).toBe(3);
    });

    test('Query allMyNewsConnection with AFTER argument', async () => {
        const response = await axios.post(server, {
            query:
            `{
                allMyNewsConnection(after: "aW5kZXg6MA==") {
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        startCursor
                        endCursor
                        nodesCount
                        totalCount
                    }
                    edges {
                        cursor
                        node {
                            title
                            uuid
                            path
                        }
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.allMyNewsConnection.edges.length).toBe(2);

    });

    test('Query allMyNewsConnection with BEFORE argument', async () => {
        const response = await axios.post(server, {
            query:
            `{
                allMyNewsConnection(before: "aW5kZXg6MA==") {
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        startCursor
                        endCursor
                        nodesCount
                        totalCount
                    }
                    edges {
                        cursor
                        node {
                            title
                            uuid
                            path
                        }
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.allMyNewsConnection.edges.length).toBe(0);
    });

    //For these tests SDL file in custom-api extension-example needs to be updated:
    // Add newsByDateConnection: [NewsSDL] in the 'extend type Query{...}'
    test('Query newsByDateConnection with no arguments: should return an error', async () => {
        const response = await axios.post(server, {
            query:
            `{
                newsByDateConnection {
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        totalCount
                    }
                    edges {
                        cursor
                        node {
                            path
                            uuid
                        }
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsByDateConnection).toBeNull();

        expect(data.errors[0].message).toContain("javax.jcr.query.InvalidQueryException");
    });

    test('Query newsByDateConnection with newsByDateArgs', async () => {
        const response = await axios.post(server, {
            query:
            `{
                newsByDateConnection(newsByDateArgs: {
                    before: "2016-01-05T21:01:12.012+00:00", 
                    after:"2006-02-05T21:06:32.010+00:00"
                }) {
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        startCursor
                        endCursor
                        nodesCount
                        totalCount
                    }
                    edges {
                        node {
                            title
                            uuid
                            path
                        }
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsByDateConnection.pageInfo.totalCount).toBe(5);

        expect(data.data.newsByDateConnection.edges.length).toBe(5);
    });

    test('Query newsByDateConnnection with the FIRST argument', async () => {
        const response = await axios.post(server, {
            query:
            `{
                newsByDateConnection(newsByDateArgs: {
                    before: "2016-01-05T21:01:12.012+00:00", 
                    after:"2006-02-05T21:06:32.010+00:00"
                }, first: 2) {
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        startCursor
                        endCursor
                        nodesCount
                        totalCount
                    }
                    edges {
                        node {
                            title
                            uuid
                            path
                        }
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsByDateConnection.pageInfo.nodesCount).toBe(2);

        expect(data.data.newsByDateConnection.edges.length).toBe(2);

        expect(data.data.newsByDateConnection.edges[0].node).toHaveProperty("title",
            "Digitall Network Expands To Transportation Industry");
    });

    test('Query newsByDateConnnection with the LAST argument', async () => {
        const response = await axios.post(server, {
           query:
           `{
                newsByDateConnection(newsByDateArgs: {
                    before: "2016-01-05T21:01:12.012+00:00", 
                    after:"2006-02-05T21:06:32.010+00:00"
                }, last: 2) {
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        startCursor
                        endCursor
                        nodesCount
                        totalCount
                    }
                    edges {
                        node {
                            title
                            uuid
                            path
                        }
                    }
                }
            }`
        },  axiosConf);

        const { data } = response;

        expect(data.data.newsByDateConnection.pageInfo.nodesCount).toBe(2);

        expect(data.data.newsByDateConnection.edges.length).toBe(2);

        expect(data.data.newsByDateConnection.edges[0].node).toHaveProperty("title",
            "all-Organic Foods Network Gains New Sponsorship");

    });

    test('Query newsByDateConnection with OFFSET and LIMIT arguments', async () => {
        const response = await axios.post(server, {
            query:
            `{
                newsByDateConnection(newsByDateArgs: {
                    before: "2018-01-05T21:01:12.012+00:00", 
                    after: "2006-02-05T21:06:32.010+00:00"}, offset: 2, limit: 4) {
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        startCursor
                        endCursor
                        nodesCount
                        totalCount
                    }
                    edges {
                        node {
                            title
                            uuid
                            path
                        }
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsByDateConnection.pageInfo.nodesCount).toBe(4);

        expect(data.data.newsByDateConnection.edges.length).toBe(4);
    });

    test('Query newsByDescriptionConnection with newsByDescriptionArgs', async () => {
       const response = await axios.post(server, {
           query:
           `{
                newsByDescriptionConnection(newsByDescriptionArgs: {
                    preview: true,
                    language: "en",
                    contains: "technology is in aerospace;"
                    }) {
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        startCursor
                        endCursor
                        nodesCount
                        totalCount
                    }
                    edges {
                        node {
                            title
                            uuid
                            path
                        }
                    }
                }
            }`
       }, axiosConf);

       const { data } = response;

       expect(data.data.newsByDescriptionConnection.edges[0].node.title).toBe("Merger With Acme Space Ltd Is Official");

       expect(data.data.newsByDescriptionConnection.edges[0].node.uuid).toBe("3f3ee972-6fd9-4261-aa78-60f1258dab7e");

       expect(data.data.newsByDescriptionConnection.edges[0].node.path).toBe("/sites/digitall/home/about/" +
           "history/area-main/timeline/merger-with-acme-space-ltd-is-of");
    });

    //For these tests SDL file in custom-api extension-example needs to be updated:
    // Add myImagesByHeightConnection: [Images] in the 'extend type Query{...}'
    test('Query myImageByHeightConnection with myImageByHeightArgs', async () => {
       const response = await axios.post(server, {
           query:
           `{
                myImagesByHeightConnection(myImagesByHeightArgs: {
                    preview: true,
                    gt: 500
                }) {
                    pageInfo {
                        hasNextPage
                        hasPreviousPage
                        startCursor
                        endCursor
                        nodesCount
                        totalCount
                    }
                    edges {
                        cursor
                        node {
                            uuid
                            path
                        }
                    }
                }
            }`
       }, axiosConf);

       const { data } = response;

       expect(data.data.myImagesByHeightConnection.pageInfo.nodesCount).toBe(99);

       expect(data.data.myImagesByHeightConnection.edges.length).toBe(99);
    });
});
