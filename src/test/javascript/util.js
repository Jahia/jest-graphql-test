const splitToArray = function(string, separator) {
    if (!string) {
        return [];
    }
    return string.split(separator)
};

module.exports = {
    splitToArray
};
