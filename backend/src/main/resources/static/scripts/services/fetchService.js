import { getCache, setCache } from "./cacheService.js";

const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

/**
 * Fetch data from the server
 *
 * @param {string} endpoint
 * @param {"GET" | "POST" | "PUT" | "DELETE"} method
 * @param {object} params
 * @param {boolean} [params.useBaseUrl]
 * @param {boolean} [params.cacheData]
 * @param {"JSON" | "TEXT"} [params.reponseType]
 * @param {"JSON" | "OTHER"} [params.requestType]
 * @param {object} [params.body]
 * @returns {Promise<any>}
*/
export async function fetchData(endpoint, method, params = {}) {
    const {
        useBaseUrl = true,
        cacheData = true,
        reponseType = "JSON",
        requestType = "JSON",
        body = null
    } = params;

    const BASE_URL = useBaseUrl ? "/api" : "";
    const enctype = requestType === "JSON" ? "application/json" : "";
    const url = `${BASE_URL}${endpoint}`;

    // Check if the data is in the cache
    if (cacheData) {
        const cache = getCache(url);
        if (cache) return cache;
    }

    // Set headers
    const headers = {};
    if (enctype) headers["Content-Type"] = enctype;
    headers[CSRF_HEADER] = CSRF_TOKEN;

    // Set options
    const options = {};
    if (body) options.body = body;
    options["method"] = method || "GET";
    options["headers"] = headers;

    // Fetch the data
    const response = await fetch(url, options);

    let result;
    if (reponseType === "JSON") result = await response.json();
    else result = await response.text();

    if (!response.ok) {
        throw new Error(result.message || "Failed to fetch data");
    }

    // Save the data in the cache
    if (cacheData) setCache(url, result);

    return result;
}