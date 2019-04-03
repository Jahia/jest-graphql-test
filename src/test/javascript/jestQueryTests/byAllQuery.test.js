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

//TESTRAIL ID: C1564369
describe('Graphql Query Tests - Query by ALL tests', () => {

    test('allTestNews query test: successful response', async () => {
        const response = await axios.post(server, {
            query:
            `{
                allTestNews (language:"en") {
                    title(language:"de")
                    description(language:"fr")
                    date
                    uuid
                    path
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.allTestNews.length).toBe(9);

        const titles = [];
        const descs = [];
        const dates = [];
        const uuids = [];
        const paths = [];

        _.forEach(data.data.allTestNews, value => {
            titles.push(value.title);
            descs.push(value.description);
            dates.push(value.date);
            uuids.push(value.uuid);
            paths.push(value.path);
        });

        _.forEach(titles, value => {
            const titleValidation = _.isString(value);
            expect(titleValidation).toBeTruthy();
        });

        _.forEach(descs, value => {
            const descValidation = _.isString(value);
            expect(descValidation).toBeTruthy();
        });

        _.forEach(dates, value => {
            const dateValidation = _.isString(value);
            expect(dateValidation).toBeTruthy();
        });

        _.forEach(uuids, value => {
            const uuidValidation = _.isString(value);
            expect(uuidValidation).toBeTruthy();
        });

        _.forEach(paths, value => {
            const pathValidation = _.isString(value);
            expect(pathValidation).toBeTruthy();
        });
    });

    test('allTestNews query test: error - unknown field', async () => {
        const response = await axios.post(server, {
            query:
                `{
                allTestNews (language:"en") {
                    title(language:"de")
                    description(language:"fr")
                    date
                    uuid
                    path
                    file
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.errors[0].description).toBe("Field 'file' in type 'TestNews' is undefined");
    });


    test('allTestNews query test: error - unknown field argument', async () => {
        const response = await axios.post(server, {
            query:
                `{
                allTestNews (language:"en") {
                    title(language:"de", sortOrder: ASC)
                    description(language:"fr")
                    date
                    uuid
                    path
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.errors[0].description).toBe("Unknown field argument sortOrder");
    });

    test('allTestNews query test: successful response with sortBy property', async () => {
        const response = await axios.post(server, {
            query:
                `{
                allTestNews(language: "en", sortBy: {
                    fieldName: "title",
                    sortType: ASC,
                    ignoreCase:true
                    }) {
                    title(language: "de")
                    description(language: "fr")
                    date
                    path
                }
            }`
        }, axiosConf);

        const  { data } = response;

        expect(data.data.allTestNews.length).toBe(9);
        expect(data.data.allTestNews[0]).toHaveProperty("title", "All-Movies erweitert seine Urlaubsfilme");
        expect(data.data.allTestNews[8]).toHaveProperty("title", "Neues Kapital für die Digitall Gruppe");

    });

    test('allTestNews query test: error - invalid sortType', async () => {
       const response = await axios.post(server, {
          query:
              `query all {
              allTestNews(language: "en", sortBy: {
                fieldName: "foo",
                sortType: DOWN,
                ignoreCase:true
              }) {
                title(language: "de")
                description(language: "fr")
                date
                path
              }
          }`
       });

       const { data } = response;

        expect(data.errors[0].description).toBe("argument 'sortBy.sortType' with value 'EnumValue{name='DOWN'}' is not a valid 'SortType'");
    });
});
