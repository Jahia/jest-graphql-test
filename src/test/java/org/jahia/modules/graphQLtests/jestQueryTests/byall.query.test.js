import axios from 'axios';

//server
const server = 'http://localhost:8080/modules/graphql';

//headers config
const axiosConf = {
    headers: {
        authorization: 'Basic cm9vdDpyb290MTIzNA=='
    }
};

describe('Graphql Query Tests - Query by ALL tests', () => {

    test('allNewsSDL query test: successful response', async () => {
        const response = await axios.post(server, {
            query:
            `{
                allNewsSDL (language:"en") {
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

    test('allNewsSDL query test: error - unknown field', async () => {
        const response = await axios.post(server, {
            query:
                `{
                allNewsSDL (language:"en") {
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


    test('allNewsSDL query test: error - unknown field argument', async () => {
        const response = await axios.post(server, {
            query:
                `{
                allNewsSDL (language:"en") {
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

    test('allNewsSDL query test: successful response with sortBy property', async () => {
        const response = await axios.post(server, {
            query:
                `{
                allNewsSDL(language: "en", sortBy: {
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

    test('allNewsSDL query test: error - invalid sortType', async () => {
       const response = await axios.post(server, {
          query:
              `query all {
              allNewsSDL(language: "en", sortBy: {
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
