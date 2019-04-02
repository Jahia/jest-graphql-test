import axios from 'axios';
import constants from '../constants';

const _ = require('lodash');
const server = process.env[constants.TEST_URL];

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
        const response1 = await axios.post(server, {
            query:
            `{
                allMyNewsConnection {
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

        expect(response1.data.data.allMyNewsConnection.edges.length).toBe(3);
        const cursor = response1.data.data.allMyNewsConnection.edges[1].cursor;

        const response = await axios.post(server, {
            query:
            `{
                allMyNewsConnection(after: "${cursor}") {
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

        expect(data.data.allMyNewsConnection.edges.length).toBe(1);

    });

    test('Query allMyNewsConnection with BEFORE argument', async () => {
        const response1 = await axios.post(server, {
            query:
                `{
                allMyNewsConnection {
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

        const cursor = response1.data.data.allMyNewsConnection.edges[2].cursor;

        const response = await axios.post(server, {
            query:
            `{
                allMyNewsConnection(before: "${cursor}") {
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

        expect(data.errors[0].message).toContain("By date range data fetcher needs at least one argument of 'after', 'before' or 'lastDays'");
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

        let edges = response.data.data.newsByDateConnection.edges;
        let titles = [];
        let uuids = [];
        let paths = [];
        _.forEach(edges, value => {
            titles.push(value.node.title);
            uuids.push(value.node.uuid);
            paths.push(value.node.path);
        });

        expect(data.data.newsByDateConnection.pageInfo.totalCount).toBe(5);
        expect(data.data.newsByDateConnection.edges.length).toBe(5);
        expect(titles.length).toBe(5);
        expect(uuids.length).toBe(5);
        expect(paths.length).toBe(5);
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

        let edges = response.data.data.newsByDateConnection.edges;
        let titles = [];
        let uuids = [];
        let paths = [];
        _.forEach(edges, value => {
            titles.push(value.node.title);
            uuids.push(value.node.uuid);
            paths.push(value.node.path);
        });

        expect(data.data.newsByDateConnection.pageInfo.nodesCount).toBe(2);
        expect(data.data.newsByDateConnection.edges.length).toBe(2);
        expect(titles.length).toBe(2);
        expect(uuids.length).toBe(2);
        expect(paths.length).toBe(2);
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

        let edges = response.data.data.newsByDateConnection.edges;
        let titles = [];
        let uuids = [];
        let paths = [];
        _.forEach(edges, value => {
            titles.push(value.node.title);
            uuids.push(value.node.uuid);
            paths.push(value.node.path);
        });

        expect(data.data.newsByDateConnection.pageInfo.nodesCount).toBe(2);
        expect(data.data.newsByDateConnection.edges.length).toBe(2);
        expect(titles.length).toBe(2);
        expect(uuids.length).toBe(2);
        expect(paths.length).toBe(2);
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

        let edges = response.data.data.newsByDateConnection.edges;
        let titles = [];
        let uuids = [];
        let paths = [];
        _.forEach(edges, value => {
            titles.push(value.node.title);
            uuids.push(value.node.uuid);
            paths.push(value.node.path);
        });

        expect(data.data.newsByDateConnection.pageInfo.nodesCount).toBe(4);
        expect(data.data.newsByDateConnection.edges.length).toBe(4);
        expect(titles.length).toBe(4);
        expect(uuids.length).toBe(4);
        expect(paths.length).toBe(4);
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

        let edges = response.data.data.newsByDateConnection.edges;
        let titles = [];
        let uuids = [];
        let paths = [];
        _.forEach(edges, value => {
            titles.push(value.node.title);
            uuids.push(value.node.uuid);
            paths.push(value.node.path);
        });

        expect(data.data.newsByDescriptionConnection.pageInfo.nodesCount).toBe(1);
        expect(data.data.newsByDescriptionConnection.edges.length).toBe(1);
        expect(titles.length).toBe(1);
        expect(uuids.length).toBe(1);
        expect(paths.length).toBe(1);
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

    test('Query myImageByHeightConnection with AFTER and BEFORE arguments', async () => {
        const response1 = await axios.post(server, {
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

        const afterCursor = response1.data.data.myImagesByHeightConnection.edges[10].cursor;
        const beforeCursor = response1.data.data.myImagesByHeightConnection.edges[50].cursor;

        const response = await axios.post(server, {
            query:
                `{
                myImagesByHeightConnection(myImagesByHeightArgs: {
                    preview: true,
                    gt: 500
                }, after: "${afterCursor}", before: "${beforeCursor}" ) {
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

        expect(data.data.myImagesByHeightConnection.pageInfo.nodesCount).toBe(39);

        expect(data.data.myImagesByHeightConnection.pageInfo.totalCount).toBe(99);

        expect(data.data.myImagesByHeightConnection.edges.length).toBe(39);

    });

    test('Query myImageByHeightConnection with sortBy filter', async () => {
        const response1 = await axios.post(server, {
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

        expect(response1.data.data.myImagesByHeightConnection.edges.length).toBe(99);
        const afterCursor = response1.data.data.myImagesByHeightConnection.edges[10].cursor;
        const beforeCursor = response1.data.data.myImagesByHeightConnection.edges[50].cursor;

        const response = await axios.post(server, {
            query:
            `{
                myImagesByHeightConnection(myImagesByHeightArgs: {
                    preview: true,
                    gt: 500,
                  sortBy: {fieldName: "height"}
                }, after: "${afterCursor}", before: "${beforeCursor}")  {
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

        expect(data.data.myImagesByHeightConnection).not.toBeNull();
    });
});
