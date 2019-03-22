import axios from 'axios';

let {isFreePort} = require('node-port-check');

//server
let server = 'http://dev.org:8081/qa/modules/graphql';

if (!isFreePort(8080) || !isFreePort(8030)){
    server = 'http://localhost:8030/qa/modules/graphql';
}

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

        expect(data.data.myImagesByHeight[0]).toHaveProperty("height", 550);
        expect(data.data.myImagesByHeight[1]).toHaveProperty("height", 631);
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

        expect(data.data.myImagesByHeight[0]).toHaveProperty("height", 1468);
        expect(data.data.myImagesByHeight[1]).toHaveProperty("height", 1440);
        expect(data.data.myImagesByHeight[97]).toHaveProperty("height", 631);
        expect(data.data.myImagesByHeight[98]).toHaveProperty("height", 550);

    });
});
