import axios from 'axios';
import constants from '../constants';

const splitToArray = require('../util.js').splitToArray;
const _ = require('lodash');
const server = process.env[constants.TEST_URL];

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

        const { data } = response;
        let companies = data.data.companyByIndustry;

        expect(data.data.companyByIndustry.length).toBe(companies.length);
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

        let edges = data.data.companyByIndustryConnection.edges;
        let titles = [];
        let overviews = [];

        _.forEach(edges, value => {
            titles.push(value.node.title);
            overviews.push(value.node.overview);
        });

        let firstTitle = splitToArray(titles[0], "");
        let secondTitle = splitToArray(titles[1], "");
        let thirdTitle = splitToArray(titles[2], "");
        let firstOverview = splitToArray(overviews[0], "");
        let secondOverview = splitToArray(overviews[1], "");
        let thirdOverview = splitToArray(overviews[2], "");

        expect(firstTitle.length).toBeGreaterThan(0);
        expect(secondTitle.length).toBeGreaterThan(0);
        expect(thirdTitle.length).toBeGreaterThan(0);
        expect(firstOverview.length).toBeGreaterThan(0);
        expect(secondOverview.length).toBeGreaterThan(0);
        expect(thirdOverview.length).toBeGreaterThan(0);

        expect(data.data.companyByIndustryConnection.edges.length).toBe(3);

    });

});
