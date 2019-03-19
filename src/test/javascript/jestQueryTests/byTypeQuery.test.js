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

//[StoryID('S7322','C1564370')]
describe('Graphql Query Tests - Query by TYPE tests', () => {

    test('test by default type: Id - successful response', async () => {
       const response = await axios.post(server, {
            query:
            `{
                testNewsById(id: "c81d50fd-7807-46ae-a4e1-96b5ba9eb2e5") {
                title
                }
            }`
       }, axiosConf);

       const { data } = response;

       expect(data.data.newsSDLById).toHaveProperty("title", "Digitall Network Expands To Transportation Industry");
    });

    test('test by default type: Id - error test: no nodes exist with specified Id', async () => {
        const response = await axios.post(server, {
            query:
            `{
                testNewsById(id: "c81d50fd-7807-96ae-a4e1-96b5ba9eb2e5") {
                title
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsSDLById).toBeNull();
    });

    test('test by default type: Path - successful response', async () => {
        const response = await axios.post(server, {
            query:
            `{
                testNewsByPath(path: "/sites/digitall/home/about/history/area-main/timeline/baumquist-joins-digitall-as-cont") {
                title
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsSDLByPath).toHaveProperty("title", "Baumquist Joins Digitall As Controller");
    });

    test('test by default type: Path - error test: specified path is invalid', async () => {
        const response = await axios.post(server, {
            query:
            `{
                testNewsByPath(path: "/sites/digitall/home/about/area-main/timeline/non-existent") {
                title
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsSDLByPath).toBeNull();
    });

    test('test by declared type: Date - successful response with BEFORE argument only', async () => {
        const response = await axios.post(server, {
            query:
                `{
                    myNewsByDate(before: "2016-01-05T21:01:12.012+00:00") {
                    title
                    description
                    date
                    }
                }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.myNewsByDate.length).toBe(8);
        expect(data.data.myNewsByDate[0].tite).not.toBeNull();
        expect(data.data.myNewsByDate[0].description).not.toBeNull();
        expect(data.data.myNewsByDate[0].date).not.toBeNull();
    });

    test('test by declared type: Date - successful response with AFTER argument only', async () => {
        const response = await axios.post(server, {
            query:
                `{
                    myNewsByDate(after: "2006-02-05T21:06:32.010+00:00") {
                    title
                    description
                    date
                    }
                }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.myNewsByDate.length).toBe(6);
    });

    test('test by declared type: Date - successful response with BEFORE and AFTER arguments', async () => {
       const response = await axios.post(server, {
           query:
           `{
                myNewsByDate(before: "2016-01-05T21:01:12.012+00:00", after: "2006-02-05T21:06:32.010+00:00") {
                    title
                    description
                    date
                }
           }`
       }, axiosConf);

       const { data } = response;

       expect(data.data.myNewsByDate.length).toBe(5);
    });

    test('test by declared type: Date - error test: BEFORE argument is out of scope', async () => {
        const response = await axios.post(server, {
            query:
            `{
                myNewsByDate(before: "1001-01-05T21:01:12.012+00:00") {
                    title
                    description
                    date
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.myNewsByDate.length).toBe(0);
    });

    test('test by declared type: Date - error test: AFTER argument is out of scope', async () => {
        const response = await axios.post(server, {
            query:
            `{
                myNewsByDate(after: "2020-01-05T21:01:12.012+00:00") {
                    title
                    description
                    date
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.myNewsByDate.length).toBe(0);
    });

    test('test by declared type: Date - error test: BEFORE and AFTER arguments are out of scope', async () => {
        const response = await axios.post(server, {
            query:
                `{
              myNewsByDate(before: "1001-01-05T21:01:12.012+00:00", after: "2020-01-05T21:01:12.012+00:00") {
                title
                description
                date
              }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.myNewsByDate.length).toBe(0);
    });

    test('test by declared type: Date - error test: BEFORE and/or AFTER properties are invalid', async () => {
        const response = await axios.post(server, {
            query:
            `{
              myNewsByDate(before: "1001-01-05T21:01:12.12+00:00", after: "2020-01-0521:01:12.012+00:00") {
                title
                description
                date
              }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.errors[0]).toHaveProperty("message",
            "javax.jcr.ValueFormatException: not a date string: 2020-01-0521:01:12.012+00:00");
    });

    test('test by declared type: Checked - successful response with VALUE set to TRUE', async () => {
        const response = await axios.post(server, {
            query:
            `{
                newsByChecked(value: true) {
                    title
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsByChecked.length).toBe(9);
    });

    test('test by declared type: Checked - successful response with VALUE set to FALSE', async () => {
        const response = await axios.post(server, {
            query:
            `{
                newsByChecked(value: false) {
                    title
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.newsByChecked.length).toBe(0);
    });

    test('test by declared type: Checked - error: VALUE property is invalid', async () => {
       const response = await axios.post(server, {
          query:
          `{
                newsByChecked(value: no) {
                    title
                }
            }`
       }, axiosConf);

       const { data } = response;

       expect(data.errors[0]).toHaveProperty("description",
           "argument 'value' with value 'EnumValue{name='no'}' is not a valid 'Boolean'");
    });

    test('test by declared type: Height with Greater Than argument', async () => {
       const response = await axios.post(server, {
           query:
           `{
                myImagesByHeight(preview: true, gt: 500) {
                    height
                }
            }`
       }, axiosConf);

       const { data } = response;

       expect(data.data.myImagesByHeight.length).toBe(99);

       expect(data.data.myImagesByHeight[0].height).toBe(1280);

       expect(data.data.myImagesByHeight[98].height).toBe(515);
    });

    test('test by declared type: Height with Less Than argument', async () => {
        const response = await axios.post(server, {
            query:
                `{
                myImagesByHeight(preview: true, lt: 500) {
                    height
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.myImagesByHeight.length).toBe(143);

        expect(data.data.myImagesByHeight[0].height).toBe(100);

        expect(data.data.myImagesByHeight[142].height).toBe(16);
    });
});
