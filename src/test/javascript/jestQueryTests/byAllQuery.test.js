import axios from 'axios';

const server = 'http://dev.org:8081/qa/modules/graphql';


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

        expect(data.data.allNewsSDL.length).toBe(9);
        expect(data.data.allNewsSDL[0].title).not.toBeNull();
        expect(data.data.allNewsSDL[0].description).not.toBeNull();
        expect(data.data.allNewsSDL[0].date).not.toBeNull();
        expect(data.data.allNewsSDL[0].uuid).not.toBeNull();
        expect(data.data.allNewsSDL[0].path).not.toBeNull();
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

        expect(data.errors[0].description).toBe("Field 'file' in type 'NewsSDL' is undefined");
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

        expect(data.data.allNewsSDL[0]).toHaveProperty("title", "All-Movies erweitert seine Urlaubsfilme");
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
