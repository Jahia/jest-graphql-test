const splitToArray = function(string, separator) {
    if (!string) {
        return [];
    }
    return string.split(separator)
};

const isValidTimeStamp = function(timestamp) {
    return (new Date(timestamp)).getTime() > 0;
};

module.exports = {
    splitToArray,
    isValidTimeStamp
};
