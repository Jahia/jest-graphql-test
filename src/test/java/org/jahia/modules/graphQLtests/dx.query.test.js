import axios from 'axios';

//server
const server = 'http://localhost:8080/modules/graphql';

//queries
const newsEntryByDateQuery = `query { myNewsByDate(before: "2016-01-05T21:01:12.012+00:00"){ title description date} }`;
const newsEntryByCheckedQuery = `query { newsByChecked(value: true) { title description date checked} }`;
const allArticlesQuery = `query { allArticles { title(language: "en") description(language:"en")  author } }`;

//headers config
const axiosConf = {
    headers: {
        authorization: 'Basic cm9vdDpyb290MTIzNA=='
    }
};

describe('DXM - Graphql Query Tests', () => {

    test('news entry by date query', async() => {
       const response = await axios.post(server, {
            query: newsEntryByDateQuery
       }, axiosConf);

        const { data } = response;

        expect(data.data.allNews.length).toBe(8);
        let i;
        for (i = 0; i < 10; i++){
            expect.not(data.data.allNews[i].title).toBeNull();
            expect.not(data.data.allNews[i].description).toBeNull();
            expect.not(data.data.allNews[i].date).toBeNull();
        }
    });

    test('allArticles query', async() => {
        const response = await axios.post(server, {
            query: allArticlesQuery
        }, axiosConf);

        const { data } = response;

        expect(data.data.allArticles.length).toBe(9);
        expect(data.data.allArticles[0].title).toBe("all-Organic Foods Network Gains New Sponsorship");
        expect(data.data.allArticles[0]).toHaveProperty("author", "root");
    });

    test('newsByChecked - Value = true', async() => {
        const response = await axios.post(server, {
            query: newsEntryByCheckedQuery
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsByChecked.length).toBe(9);
        for (i = 0; i < 10; i++){
            expect(data.data.allNews[i].checked).toBeTruthy();
        }
    });
});

function createNewsEntryQuery(type, id, path, preview, locale, checked, after, before, lastDays) {
    switch (type) {
        case "byId":
            return `{ newsById(id: "`+ id +`"){ uuid } }`;
        case "byPath":
            return `{ newsByPath(path: "`+ path +`"){ uuid } }`;
        case "byDate":
            if (after != null && before === null && lastDays === null) {
                return `{ myNewsByDate(after: "`+ after +`"){ title } }`;
            }
            else if (before != null && after === null && lastDays === null) {
                return `{ myNewsByDate(before: "`+ before +`"){ title } }`;
            }
            else if (after != null && before != null && lastDays === null) {
                return `{ myNewsByDate(before: "`+ before +`", after: "`+ after +`"){ title } }`;
            }
            else if (lastDays != null) {
                return `{ myNewsByDate(lastdays: `+ lastDays +`){ title } }`;
            }
            break;
        case "byChecked":
            return `{ newsByChecked(value: `+ checked +`) { title } }`;

    }
}
