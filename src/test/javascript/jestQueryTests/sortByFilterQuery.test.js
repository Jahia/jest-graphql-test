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

describe('GraphQL Test - sortBy filter', () => {

    test('sortBy - (fieldName: title, sortType: ASC, ignoreCase: true)', async () => {
       const response = await axios.post(server, {
            query:
            `{
                myNewsByDate(before: "2019-02-11T15:24:06.023+00:00", sortBy: {
                    fieldName: "title", 
                    sortType: ASC, 
                    ignoreCase: true}) {
                    title
                }
            }`
       }, axiosConf);

       const { data } = response;

       expect(data.data.myNewsByDate[0].title).toBe("all-Movies Adds To Holiday Movies");

       expect(data.data.myNewsByDate[8].title).toBe("New Investment Account For Digitall Network");
    });

    test('sortBy - (feildName: title, sortType: DESC, ignoreCase: false)', async () => {
        const response = await axios.post(server, {
            query:
            `{
                myNewsByDate(before: "2019-02-11T15:24:06.023+00:00", sortBy: {
                    fieldName: "title", 
                    sortType: DESC, 
                    ignoreCase: false}) {
                    title
                }
            }`
        }, axiosConf);

        const { data } =  response;

        expect(data.data.myNewsByDate[0].title).toBe("all-Organic Foods Network Gains New Sponsorship");

        expect(data.data.myNewsByDate[8].title).toBe("Baumquist Joins Digitall As Controller");
    });

    test('sortBy filter with invalid fieldName', async () => {
       const response = await axios.post(server, {
           query:
           `{
                myNewsByDate(before: "2019-02-11T15:24:06.023+00:00" sortBy: {
                    fieldName: "file",
                    sortType: ASC,
                    ignoreCase: true}) {
                    title
                }
            }`
       }, axiosConf);

        const { data } =  response;

        expect(data.errors[0]).toHaveProperty("message", "Cannot sort by invalid field name 'file'");

    });

    test('sortBy filter with invalid sortType', async () => {
        const response = await axios.post(server, {
            query:
                `{
                myNewsByDate(before: "2019-02-11T15:24:06.023+00:00" sortBy: {
                    fieldName: "title",
                    sortType: UP,
                    ignoreCase: true}) {
                    title
                }
            }`
        }, axiosConf);

        const { data } =  response;

        expect(data.errors[0]).toHaveProperty("description",
            "argument 'sortBy.sortType' with value 'EnumValue{name='UP'}' is not a valid 'SortType'")
    });

    test('sortBy filter on number fields with ASC sortType', async () => {
        const response = await axios.post(server, {
            query:
            `{
                myImagesByHeight(gt: 500, sortBy: {fieldName: "height", sortType: ASC}) {
                    height
                }
            }`
        }, axiosConf);

        const { data } = response;

        const heights = [];
        _.forEach(data.data.myImagesByHeight, value => {
            heights.push(value.height);
        });

        const minHeight = _.min(heights);
        const maxHeight = _.max(heights);

        expect(data.data.myImagesByHeight[0]).toHaveProperty("height", minHeight);
        expect(data.data.myImagesByHeight[98]).toHaveProperty("height", maxHeight);

    });

    test('sortBy filter on number fields with DESC sortType', async () => {
        const response = await axios.post(server, {
            query:
                `{
                myImagesByHeight(gt: 500, sortBy: {fieldName: "height", sortType: DESC}) {
                    height
                }
            }`
        }, axiosConf);

        const { data } = response;

        const heights = [];
        _.forEach(data.data.myImagesByHeight, value => {
           heights.push(value.height);
        });

        const minHeight = _.min(heights);
        const maxHeight = _.max(heights);

        expect(data.data.myImagesByHeight[0]).toHaveProperty("height", maxHeight);
        expect(data.data.myImagesByHeight[98]).toHaveProperty("height", minHeight);

    });

    //add sortBy on metadata eg. fieldName: "metadata.created"

    test('sortBy filter on metadata.created field with ASC sortType', async () => {
        const response1 = await axios.post(server, {
            query:
                `{
                allCompany {
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

        let sortedCompanies = _.orderBy(response1.data.data.allCompany, ['metadata.created'], ['ASC']);

        const response = await axios.post(server, {
            query:
                `{
                allCompany(sortBy: {fieldName: "metadata.created", sortType: ASC}) {
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

        const {data} = response;

        expect(data.data.allCompany[0]).toMatchObject(sortedCompanies[0]);

    });

    test('sortBy filter on metadata.created field with DESC sortType', async () => {
        const response1 = await axios.post(server, {
            query:
                `{
            allCompany {
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

        let sortedCompanies = _.orderBy(response1.data.data.allCompany, ['metadata.created'], ['DESC']);

        const response = await axios.post(server, {
            query:
                `{
            allCompany(sortBy: {fieldName: "metadata.created", sortType: DESC}) {
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

        expect(data.data.allCompany[0]).toMatchObject(sortedCompanies[0]);
    });
});
