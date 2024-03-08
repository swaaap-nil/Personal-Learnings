async function fetchData(shortName, version, time, boundingBox, agent, format, coverage, projection, page_size, page_num, bearerToken) {
    const baseUrl = 'https://n5eil02u.ecs.nsidc.org/egi/request';
  
    const queryParams = new URLSearchParams();
    queryParams.set('short_name', shortName);
    queryParams.set('version', version);
    queryParams.set('time', time);
    queryParams.set('bounding_box', boundingBox);
    queryParams.set('agent', agent);
  
    if (format) queryParams.set('format', format);
    if (coverage) queryParams.set('Coverage', coverage);
    if (projection) queryParams.set('projection', projection);
    if (page_size) queryParams.set('page_size', page_size);
    if (page_num) queryParams.set('page_num', page_num);
    
    console.log(queryParams.toString());

    const url = `${baseUrl}?${queryParams.toString()}`;
  
    const authHeaders = new Headers({
      'Authorization': `Bearer ${bearerToken}`,
      // You may need other headers here based on the API requirements
    });
  
    const requestOptions = {
      method: 'GET', // You can change this method as needed (e.g., 'POST')
      headers: authHeaders,
    };
  
    try {
      const response = await fetch(url, requestOptions);
      if (response.ok) {
        const data = await response.json(); // You can use response.text() for non-JSON data
        return data;
      } else {
        throw new Error(`Failed to fetch data: ${response.status} - ${response.statusText}`);
      }
    } catch (error) {
      console.error(error);
      throw error;
    }
  }
  
  // Example usage:
  fetchData('NSIDC-0481', '1', '2017-01-01T00:00:00,2018-12-31T00:00:00', '-52.5,68.5,-47.5,69.5', 'NO', null, null, null, 100, 1, 'your_bearer_token')
    .then(data => {
      // Process the data here
      console.log(data);
    })
    .catch(error => {
      // Handle errors here
      console.error(error);
    });
  