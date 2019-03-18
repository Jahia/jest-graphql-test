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

    test('sortBy with invalid fieldName', async () => {
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

    test('sortBy with invalid sortType', async () => {
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
});
