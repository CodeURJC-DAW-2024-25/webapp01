// Cache expiration time in milliseconds (10 minutes)
const CACHE_EXPIRATION = 10 * 60 * 1000;
const CACHE_PREFIX = "cache_";

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

    try {
        localStorage.setItem(CACHE_PREFIX + key, JSON.stringify(item));
    } catch (e) {
        removeOldestCacheItem();
        localStorage.setItem(CACHE_PREFIX + key, JSON.stringify(item));
    }
}

/**
 * Get cache from localStorage
 * 
 * @param {string} key
 * @returns {any} The value stored in the cache
*/
export function getCache(key) {
    const data = localStorage.getItem(CACHE_PREFIX + key);
    if (!data) return null;

    const { value, timestamp } = JSON.parse(data);
    const now = Date.now();
    const diff = now - timestamp;

    if (diff > CACHE_EXPIRATION) {
        localStorage.removeItem(CACHE_PREFIX + key);
        return null;
    }

    return value;
}

function removeOldestCacheItem() {
    let oldestKey = null;
    let oldestTimestamp = Number.MAX_SAFE_INTEGER;

    for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i);
        if (!key.startsWith(CACHE_PREFIX)) continue;

        try {
            const { timestamp } = JSON.parse(localStorage.getItem(key));
            if (timestamp && timestamp < oldestTimestamp) {
                oldestKey = key;
                oldestTimestamp = item.timestamp;
            }
        } catch (e) {
            continue;
        }
    }

    if (oldestKey) localStorage.removeItem(oldestKey);
}