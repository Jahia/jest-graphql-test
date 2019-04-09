import axios from 'axios';
import constants from '../constants';

const splitToArray = require('../util.js').splitToArray;
const isValidTimeStamp = require('../util.js').isValidTimeStamp;
const _ = require('lodash');
const server = process.env[constants.TEST_URL];

//headers config
const axiosConf = {
    headers: {
        authorization: 'Basic cm9vdDpyb290MTIzNA=='
    }
};

describe('Graphql Query Tests - Predefined and custom types', () => {

    test('Predefined type: Metadata', async () => {
       const response = await axios.post(server, {
           query:
           `{
                allSdlTests (language: "en"){
                    metadata {
                        created
                        createdBy
                        lastModified
                        lastModifiedBy
                        lastPublished
                        lastPublishedBy
                        uuid
                        path
  	                }  
                }  
            }`
       }, axiosConf);

       const { data } = response;

       let sdlTests = data.data.allSdlTests;
       let createdValues = [];
       let lastModifiedValues = [];

       _.forEach(sdlTests, value => {
           createdValues.push(value.metadata.created);
           lastModifiedValues.push(value.metadata.lastModified);
       });

       _.forEach(createdValues, value => {
           let validTS = isValidTimeStamp(value);
           expect(validTS).toBeTruthy();
       });

        _.forEach(lastModifiedValues, value => {
            let validTS = isValidTimeStamp(value);
            expect(validTS).toBeTruthy();
        });

    });

    test('Predefined type: Asset', async () => {
        const response = await axios.post(server, {
            query:
            `query videoByPath {
                testVideoByPath(path: "/sites/digitall/home/area-main/area-1/area-1-main/global-presence-video") {
                    title
                    forceFlashPlayer
                    autoplay
                    source {
                        type
                        size
                        path
                        url
                    } 
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.testVideoByPath).toMatchObject({
            "title": "Global Presence",
            "forceFlashPlayer": false,
            "autoplay": false,
            "source": {
                "type": "video/mp4",
                "size": 444408,
                "path": "/sites/digitall/files/video/Arc de Triomphe in Paris.mp4",
                "url": "/qa/files/live/sites/digitall/files/video/Arc%20de%20Triomphe%20in%20Paris.mp4"
            }
        });
    });

    test('Predefined type: ImageAsset as a single value', async () => {
        const response = await axios.post(server, {
            query:
            `{
                allSdlTests{
                    image {
                        type
                        size
                        height
                        width
                        path
                        url
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.allSdlTests[0]).toMatchObject({
                "image": {
                    "type": "image/jpeg",
                    "size": 60777,
                    "height": 515,
                    "width": 515,
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg",
                    "url": "/qa/files/live/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg"
                }
            },
            {
                "image": {
                    "type": "image/jpeg",
                    "size": 60777,
                    "height": 515,
                    "width": 515,
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg",
                    "url": "/qa/files/live/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg"
                }
            },
            {
                "image": {
                    "type": "image/jpeg",
                    "size": 60777,
                    "height": 515,
                    "width": 515,
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg",
                    "url": "/qa/files/live/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg"
                }
            });
    });

    test('Predefined type: ImageAsset as a list', async () => {
        const response = await axios.post(server, {
            query:
            `{
                allSdlTests{
                    photos {
                        type
                        size
                        height
                        width
                        uuid
                        path
                        url
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.allSdlTests).toHaveLength(3);
    });

    test('Custom predefined type: sdlSubTest', async () => {
        const response = await axios.post(server, {
            query:
            `{
                allSdlTests{
                    tests {
                        title
                        body
                        path
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        let sdlTests = data.data.allSdlTests;
        let titles = [];
        let bodies = [];
        let paths  = [];

        _.forEach(sdlTests, value => {
            titles.push(value.tests.title);
            bodies.push(value.tests.body);
            paths.push(value.tests.path);
        });

        _.forEach(titles, value => {
            let titleValidation = _.isString(value);
            expect(titleValidation).toBeTruthy();
        });

        _.forEach(bodies, value => {
            let bodyValidation = _.isString(value);
            expect(bodyValidation).toBeTruthy();
        });

        _.forEach(paths, value => {
            let pathValidation = _.isString(value);
            expect(pathValidation).toBeTruthy();
        });

    });

    test('Custom predefined type: sdlSubTest  as a list', async () => {
        const response = await axios.post(server, {
            query:
                `{
                allSdlTests{
                    testList {
                        title
                        body
                        path
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        let sdlTests = data.data.allSdlTests;
        let testLists = [];
        let titles = [];
        let bodies = [];
        let paths  = [];


        _.forEach(sdlTests, value => {
            testLists.push(value.testList);
        });

        _.forEach(testLists, value => {
            titles.push(value[0].title);
            titles.push(value[1].title);
            bodies.push(value[0].body);
            bodies.push(value[1].body);
            paths.push(value[0].path);
            paths.push(value[1].path);
        });

        _.forEach(titles, value => {
            let titleValidation = _.isString(value);
            expect(titleValidation).toBeTruthy();
        });

        _.forEach(bodies, value => {
            let bodyValidation = _.isString(value);
            expect(bodyValidation).toBeTruthy();
        });

        _.forEach(paths, value => {
            let pathValidation = _.isString(value);
            expect(pathValidation).toBeTruthy();
        });

    });
});
