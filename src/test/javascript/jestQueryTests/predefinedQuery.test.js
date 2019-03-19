import axios from 'axios';

let {isFreePort} = require('node-port-check');

//server
let server = 'http://dev.org:8081/qa/modules/graphql';

// if (!isFreePort(8080) || !isFreePort(8030)){
//     server = 'http://localhost:8080/modules/graphql';
// }

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

       expect(data.data.allSdlTests[0].created).not.toBeNull();
       expect(data.data.allSdlTests[0].lastModified).not.toBeNull();
       expect(data.data.allSdlTests[0].uuid).not.toBeNull();
       expect(data.data.allSdlTests[1].created).not.toBeNull();
       expect(data.data.allSdlTests[1].lastModified).not.toBeNull();
       expect(data.data.allSdlTests[1].uuid).not.toBeNull();
       expect(data.data.allSdlTests[2].created).not.toBeNull();
       expect(data.data.allSdlTests[2].lastModified).not.toBeNull();
       expect(data.data.allSdlTests[2].uuid).not.toBeNull();

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

        expect(data.data.allSdlTests).toHaveLength(6);
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

        expect(data.data.allSdlTests[0]).toMatchObject({
                "tests": {
                    "title": "Sub test",
                    "body": "Body sub test",
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-1/subTest"
                }
            },
            {
                "tests": {
                    "title": "Sub test",
                    "body": "Body sub test",
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test/subTest"
                }
            },
            {
                "tests": {
                    "title": "Sub test",
                    "body": "Body sub test",
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-2/subTest"
                }
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

        expect(data.data.allSdlTests[0]).toMatchObject({
                "testList": [
                    {
                        "title": "Item One",
                        "body": "Item One Body",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-1/listTest/itemOne"
                    },
                    {
                        "title": "Item Two",
                        "body": "Item Two Body",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-1/listTest/itemTwo"
                    }
                ]
            },
            {
                "testList": [
                    {
                        "title": "Item One",
                        "body": "Item One Body",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test/listTest/itemOne"
                    },
                    {
                        "title": "Item Two",
                        "body": "Item Two Body",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test/listTest/itemTwo"
                    }
                ]
            },
            {
                "testList": [
                    {
                        "title": "Item One",
                        "body": "Item One Body",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-2/listTest/itemOne"
                    },
                    {
                        "title": "Item Two",
                        "body": "Item Two Body",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-2/listTest/itemTwo"
                    }
                ]
            });
    });
});
