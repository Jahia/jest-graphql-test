import axios from 'axios';
const _ = require('lodash');
import constants from '../constants';

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

        expect(data.data.myImagesByHeight[0]).toHaveProperty("height", 515);
        expect(data.data.myImagesByHeight[1]).toHaveProperty("height", 550);
        expect(data.data.myImagesByHeight[97]).toHaveProperty("height", 1440);
        expect(data.data.myImagesByHeight[98]).toHaveProperty("height", 1468);

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

        //find out about lodash MIN/MAX assertions
        expect(data.data.myImagesByHeight[0]).toHaveProperty("height", 1468);
        expect(data.data.myImagesByHeight[1]).toHaveProperty("height", 1440);
        expect(data.data.myImagesByHeight[97]).toHaveProperty("height", 550);
        expect(data.data.myImagesByHeight[98]).toHaveProperty("height", 515);

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
