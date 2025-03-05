// Cache expiration time in milliseconds (10 minutes)
const CACHE_EXPIRATION = 10 * 60 * 1000;

/**
 * Set cache in localStorage
 * 
 * @param {string} key
 * @param {any} value
*/
export function setCache(key, value) {
    const now = Date.now();
    const item = {
        value: value,
        timestamp: now
    };

    localStorage.setItem(key, JSON.stringify(item));
}

/**
 * Get cache from localStorage
 * 
 * @param {string} key
 * @returns {any} The value stored in the cache
*/
export function getCache(key) {
    const data = localStorage.getItem(key);
    if (!data) return null;

    const { value, timestamp } = JSON.parse(data);
    const now = Date.now();
    const diff = now - timestamp;

    if (diff > CACHE_EXPIRATION) {
        localStorage.removeItem(key);
        return null;
    }

    return value;
}