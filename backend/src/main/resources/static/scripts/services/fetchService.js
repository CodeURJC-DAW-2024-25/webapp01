import { getCache, setCache } from "./cacheService.js";

const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

/**
 * Fetch data from the server
 *
 * @param {string} endpoint
 * @param {string} method
 * @param {any} body
 * @returns {Promise<any>}
*/
export async function fetchData(endpoint, method, body = null) {
    const BASE_URL = "/api";
    const url = `${BASE_URL}${endpoint}`;

    // Check if the data is in the cache
    const cache = getCache(url);
    if (cache) return cache;

    const options = {
        method,
        headers: {
            "Content-Type": "application/json",
            [CSRF_HEADER]: CSRF_TOKEN
        }
    };

    if (body) options.body = JSON.stringify(body);

    const response = await fetch(url, options);
    const result = await response.json();

    if (!response.ok) {
        throw new Error(result.message || "Failed to fetch data");
    }

    // Save the data in the cache
    setCache(url, result);

    return result;
}