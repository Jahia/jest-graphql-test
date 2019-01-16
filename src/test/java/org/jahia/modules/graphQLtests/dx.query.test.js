import axios from 'axios';

//server
const server = 'http://localhost:8080/modules/graphql';

//queries
const allNewsQuery = `query {	allNews{   title(language:"en")   description(language:"en")   file   date}}`;
const allArticlesQuery = `query { allArticles { title(language: "en") description(language:"en")  author }}`;
const allArticlesMetadata = `query { allArticles { metadata { created(language: "en") }}}`;

//headers config
const axiosConf = {
    headers: {
        authorization: 'Basic cm9vdDpyb290MTIzNA=='
    }
};

describe('DXM - Graphql Query Tests', () => {

    test('allNews query', async() => {
       const response = await axios.post(server, {
            query: allNewsQuery
       }, axiosConf);

        const { data } = response;

        expect(data.data.allNews.length).toBe(9);
        var i;
        for (i = 0; i < 10; i++){
            expect.not(data.data.allNews[i].title).toBeNull();
            expect.not(data.data.allNews[i].description).toBeNull();
            expect.not(data.data.allNews[i].file).toBeNull();
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

    test('allArticles Metadata', async() => {
        const response = await axios.post(server, {
            query: allArticlesMetadata
        }, axiosConf);

        const { data } = response;

        expect(data.data.allArticles.length).toBe(9);
        expect(data.data.allArticles[3].metadata).toHaveProperty("created", "1452106851132");
        var i;
        for (i = 0; i < 10; i++) {
            expect.not(data.data.allArticles[i].metadata.created).toBeNull();
        }
    });
});
