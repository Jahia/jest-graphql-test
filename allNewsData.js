const axios = require('axios');

const server = 'http://localhost:8080/modules/graphql';
const query = `query { allNews { title(language: "en") }}`;

function fetchNewsData() {
    return axios
        .post(server, {query: query} )
        .then(response => {
            return response.data;
        });
};

exports.fetchNewsData = fetchNewsData;
