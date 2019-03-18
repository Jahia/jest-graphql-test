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

       expect(data.data.allSdlTests[0]).toMatchObject({
               "metadata": {
                   "created": "2019-03-07T15:48:43.572Z",
                   "createdBy": "root",
                   "lastModified": "2019-03-07T15:48:43.572Z",
                   "lastModifiedBy": "root",
                   "lastPublished": null,
                   "lastPublishedBy": null,
                   "uuid": "1e35f520-2728-4504-bbd3-2b9d3d8b7b02",
                   "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-1"
               }
           },
           {
               "metadata": {
                   "created": "2019-03-07T15:48:43.570Z",
                   "createdBy": "root",
                   "lastModified": "2019-03-07T15:48:43.570Z",
                   "lastModifiedBy": "root",
                   "lastPublished": null,
                   "lastPublishedBy": null,
                   "uuid": "49fadb20-de5b-4838-bfa5-70c09e57387b",
                   "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test"
               }
           },
           {
               "metadata": {
                   "created": "2019-03-07T15:48:43.574Z",
                   "createdBy": "root",
                   "lastModified": "2019-03-07T15:48:43.574Z",
                   "lastModifiedBy": "root",
                   "lastPublished": null,
                   "lastPublishedBy": null,
                   "uuid": "ffef3149-a477-43a4-a843-f25a91df5f4b",
                   "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-2"
               }
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
                        uuid
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
                "uuid": "218877e2-4f45-46fb-8c3a-f47fba21a617",
                "path": "/sites/digitall/files/video/Arc de Triomphe in Paris.mp4",
                "url": "/files/live/sites/digitall/files/video/Arc%20de%20Triomphe%20in%20Paris.mp4"
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
                        uuid
                        path
                        url
                    }
                }
            }`
        }, axiosConf);

        const { data } = response;

        expect(data.data.allSdlTests[0]).toHaveObject({
                "image": {
                    "type": "image/jpeg",
                    "size": 60777,
                    "height": 515,
                    "width": 515,
                    "uuid": "33ef3bcd-3de1-4203-8bd3-db053a3b6ae5",
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg",
                    "url": "/files/live/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg"
                }
            },
            {
                "image": {
                    "type": "image/jpeg",
                    "size": 60777,
                    "height": 515,
                    "width": 515,
                    "uuid": "33ef3bcd-3de1-4203-8bd3-db053a3b6ae5",
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg",
                    "url": "/files/live/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg"
                }
            },
            {
                "image": {
                    "type": "image/jpeg",
                    "size": 60777,
                    "height": 515,
                    "width": 515,
                    "uuid": "33ef3bcd-3de1-4203-8bd3-db053a3b6ae5",
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg",
                    "url": "/files/live/modules/sdl-tests/1.0-SNAPSHOT/templates/files/black-tshirt.jpg"
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

        expect(data.data.allSdlTests[0].length).toBe(3);
    });

    test('Custom predefined type: sdlSubTest', async () => {
        const response = await axios.post(server, {
            query:
            `{
                allSdlTests{
                    tests {
                        title
                        body
                        uuid
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
                    "uuid": "c4b4faa0-0859-49c8-98dd-cd319228495c",
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-1/subTest"
                }
            },
            {
                "tests": {
                    "title": "Sub test",
                    "body": "Body sub test",
                    "uuid": "41854866-efac-4a63-b990-04ed5da2205f",
                    "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test/subTest"
                }
            },
            {
                "tests": {
                    "title": "Sub test",
                    "body": "Body sub test",
                    "uuid": "dbe86aef-b196-40fe-aa7a-2c9012f9a0dc",
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
                        uuid
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
                        "uuid": "bbdf6cac-a98e-451c-a2a6-1a95a0d71735",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-1/listTest/itemOne"
                    },
                    {
                        "title": "Item Two",
                        "body": "Item Two Body",
                        "uuid": "5ab94c2e-5ca6-4663-aa75-f71be41d7427",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-1/listTest/itemTwo"
                    }
                ]
            },
            {
                "testList": [
                    {
                        "title": "Item One",
                        "body": "Item One Body",
                        "uuid": "cf0dedb3-8873-41ce-b77b-88a399b73350",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test/listTest/itemOne"
                    },
                    {
                        "title": "Item Two",
                        "body": "Item Two Body",
                        "uuid": "efcb24ec-4f6b-41a2-a25d-5657e7bd968a",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test/listTest/itemTwo"
                    }
                ]
            },
            {
                "testList": [
                    {
                        "title": "Item One",
                        "body": "Item One Body",
                        "uuid": "140dabfc-192f-4582-be89-dffd3c276094",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-2/listTest/itemOne"
                    },
                    {
                        "title": "Item Two",
                        "body": "Item Two Body",
                        "uuid": "b44ecc76-793f-4485-8e1e-e4e73ee6bfd8",
                        "path": "/modules/sdl-tests/1.0-SNAPSHOT/contents/test-2/listTest/itemTwo"
                    }
                ]
            });
    });
});
