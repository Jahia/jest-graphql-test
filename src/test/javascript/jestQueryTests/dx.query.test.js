import axios from 'axios';

//server
const server = 'http://dev.org:8081/qa/modules/graphql';

//queries
const newsEntryByDateQuery = `query { myNewsByDate(before: "2016-01-05T21:01:12.012+00:00"){ title description date} }`;
const newsEntryByCheckedQuery = `query { newsByChecked(value: true) { title description date checked} }`;

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

        expect(data.data.myNewsByDate.length).toBe(8);

        expect(data.data.myNewsByDate[0].title).not.toBeNull();

    });

    test('newsByChecked - Value = true', async() => {
        const response = await axios.post(server, {
            query: newsEntryByCheckedQuery
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsByChecked.length).toBe(9);

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
