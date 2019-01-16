import axios from 'axios';

describe('CIO - GraphQL Queries Test', () => {

    //Using demo01 server - (https://demo.commerceio.jahia.com)
    const SERVER = "https://demo.commerceio.jahia.com/modules/graphql";

    //TEST 1 : ProductInfo Query
    test('product info', async () => {
         const piResponse = await axios.post(SERVER,{
             query: createProductInfoQuery("apparel-uk", "en", "\"115755_uni\", \"132871_deepest_black\"")
        });

    const { data } = piResponse;

    expect(data.data.cioProductsInfo[0].priceInfo).toHaveProperty('price', 720.86);
    expect(data.data.cioProductsInfo[1].priceInfo).toHaveProperty('price', 485.96);

    });

    //TEST 2 : ShopByCategory Query
    test('brand info', async () => {
        const biResponse = await axios.post(SERVER,{
            query: createShopByCatQuery("commio01", "apparelukfull_alias_en", "null", "brands", "/Brands/Analog")
        });

        const { data } = biResponse;

        expect(data).toMatchObject({
            data: {
                cioShopByCategory: [
                    {
                        docCount: 3,
                        key: "Analog",
                        category: {
                            name: "Analog"
                        }
                    }
                ]
            }
        });
    });

    //TEST 3 : BrandCategories Query
    test('brand by categories', async () => {
       const bbcResponse = await axios.post(SERVER, {
           query: createBrandsCatQuery("commio01", "apparelukfull_categories__alias_en", "\"Analog\",\"DC\"")
       });

       const { data } = bbcResponse;

       expect(data.data.cioBrandCategories.length).toBeLessThan(3);
       expect(data.data.cioBrandCategories[0]).toHaveProperty('name', 'Analog');
       expect(data.data.cioBrandCategories[1]).toHaveProperty('name', 'DC');
    });

    //TEST 4 : ProductList Query
    test('product list', async () => {
        const plResponse = await axios.post(SERVER, {
            query: createProductListQuery("commio01", "apparelukfull_alias_en", "/categories/surf", 0, 5)
        });

        const { data } = plResponse;

        expect(data.data.cioProducts).toHaveLength(5);
    });

    //TEST 5 : ShopByVariant Query
    test('shop by variant', async () => {
        const sbvResponse = await axios.post(SERVER, {
            query: createShopByVariantsQuery("commio01", "apparelukfull_alias_en", "/brands", "\"L\"", null)
        });

        const { data } = sbvResponse;

        expect(data.data.cioShopByVariants[0]).toHaveProperty('name', 'size');
        expect(data.data.cioShopByVariants[0].bucketList[0]).toHaveProperty('key', 'L');
    });

    //Test 6 : CatalogProducts Query
    test('products by skus', async () => {
       const pbsResponse = await axios.post(SERVER, {
           query: createCatalogProductsQuery("commio01", "apparelukfull_alias_en", "\"115755_uni\",\"115754_uni\"")
       });
       const { data } = pbsResponse;

       expect(data.data.cioProductsBySku.length).toEqual(2);
       expect(data.data.cioProductsBySku[0].name).toBe('Ekeel 5.11 G/O Fish uni');
       expect(data.data.cioProductsBySku[1].name).toBe('Ekeel 6.3 G/O Fish uni');
    });
});

//siteKey eg. 'apparel-uk', local eg. 'en', skus eg. '"skuprod1","skuprod2",..'
function createProductInfoQuery(siteKey, locale, skus) {
    return `
    query ProductsInfo {
      cioProductsInfo(siteKey: "`+siteKey+`", language: "`+locale+`", productCodes: [`+skus+`]) {
        sku
        inStock
        priceInfo {
          currencyIso
          price
          formattedPrice
        }
        variantsProductInfo {
          sku
        }
      }
    }
    `
}

//conn eg. '[ES Connection ID]', index eg. '[ES Index Alias]',
function createShopByCatQuery(conn, index, config, type, category) {
    return `
    query ShopByCategory {
        cioShopByCategory(connection: "`+conn+`", index: "`+index+`",
                            config: `+config+`, type:"`+type+`", category:"`+category+`")	{
            docCount
            key
            category {
                name
            }
        }
    }
    `
}

function createBrandsCatQuery(conn, index, categories) {
    return `
    query BrandCategories {
      cioBrandCategories(connection: "`+conn+`", index: "`+index+`", categoriesNames: [`+categories+`]) {
        name
        idPath
        path
        image {
          format
          url
        }
      }
    }
    `
}

function createProductListQuery(conn, index, category, offset, limit, config) {
    return `
    query ProductList{
        cioProducts(connection:"`+conn+`", index:"`+index+`",
            category: "`+category+`", offset:`+offset+`, limit:`+limit+`, config: "null") {
            sku
            name 
            mountedPath
            summary
            vanityUrl
            price{
                formattedValue
            }
            images {
                format
                imageType
                url
            }
  			}
    }
    `
}

function createShopByVariantsQuery(conn, index, category, sizeValues, styleValues) {
    let config;
    if (sizeValues != null && styleValues != null) {
        config = "{ \n" +
            "search: null, \n" +
            "facets: { \n" +
                "shopByCategory: { \n" +
                    "selectedCategories: [], \n" +
                    "currentPath: \""+category+"\" \n" +
                "}, \n" +
                "shopByVariants: { \n" +
                    "selectedVariants: [{name: \"size\", value: ["+sizeValues+"]}, {name: \"style\", value: ["+styleValues+"]}], \n" +
                    "currentPath: \""+category+"\" \n" +
                "} \n " +
            "} \n" +
        "}";
    } else if (sizeValues != null && styleValues == null) {
        config = "{ \n" +
            "search: null, \n" +
            "facets: { \n" +
                "shopByCategory: { \n" +
                    "selectedCategories: [], \n" +
                    "currentPath: \""+category+"\" \n" +
                "}, \n" +
                "shopByVariants: { \n" +
                    "selectedVariants: [{name: \"size\", value: ["+sizeValues+"]}], \n" +
                    "currentPath: \""+category+"\" \n" +
                "} \n " +
            "} \n" +
        "}";
    } else if (sizeValues == null && styleValues != null) {
        config = "{ \n" +
            "search: null, \n" +
            "facets: { \n" +
                "shopByCategory: { \n" +
                    "selectedCategories: [], \n" +
                    "currentPath: \""+category+"\" \n" +
                "}, \n" +
                "shopByVariants: { \n" +
                    "selectedVariants: [{name: \"style\", value: ["+styleValues+"]}], \n" +
                    "currentPath: \""+category+"\" \n" +
                "} \n " +
            "} \n" +
        "}";
    }
    return `
    query shopByVariants{
        cioShopByVariants(connection: "`+conn+`", index: "`+index+`", category: "`+category+`", config: `+config+`) {
            name
            bucketList {
                key
                docCount
             }
         }
     }
     `
}

function createCatalogProductsQuery(conn, index, skus) {
    return `
    query CatalogProducts {
      cioProductsBySku (connection: "`+conn+`", index: "`+index+`", skus: [`+skus+`]) {
            sku
            name
            mountedPath
            vanityUrl
            summary
            price {
                formattedValue
            }
            images {
                format
                imageType
                url
            }
        }
    }
    `
}
