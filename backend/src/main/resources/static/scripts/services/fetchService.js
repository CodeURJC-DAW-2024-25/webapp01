import { getCache, setCache } from "./cacheService.js";

const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

/**
 * Fetch data from the server
 *
 * @param {string} endpoint
 * @param {string} method
 * @param {object} params
 * @param {boolean} params.useBaseUrl
 * @param {boolean} params.cacheData
 * @param {"JSON" | "TEXT"} params.reponseType
 * @param {object} params.body
 * @returns {Promise<any>}
*/
export async function fetchData(endpoint, method, params = {
    useBaseUrl: true,
    cacheData: true,
    reponseType: "JSON",
    body: null
}) {
    const BASE_URL = params.useBaseUrl ? "/api" : "";
    const url = `${BASE_URL}${endpoint}`;

    const { body, cacheData, reponseType } = params;

    // Check if the data is in the cache
    if (cacheData) {
        const cache = getCache(url);
        if (cache) return cache;
    }

    const options = {
        method: method || "GET",
        headers: {
            "Content-Type": "application/json",
            [CSRF_HEADER]: CSRF_TOKEN
        }
    };

    if (body) options.body = JSON.stringify(body);

    const response = await fetch(url, options);

    let result;
    if (reponseType === "JSON") result = await response.json();
    else result = await response.text();

    if (!response.ok) {
        throw new Error(result.message || "Failed to fetch data");
    }

    // Save the data in the cache
    setCache(url, result);

    return result;
}