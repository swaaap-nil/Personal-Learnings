import pandas as pd
import requests
from shapely.geometry import Point
import geopandas as gpd
import json

def get_osm_data(lat, lon, radius):
    overpass_url = "http://overpass-api.de/api/interpreter"

    query = f"""
    [out:json];
    node["amenity"="cafe"](around:{radius},{lat},{lon});
    out body;
    >;
    out skel qt;
    """

    response = requests.get(overpass_url, params={'data': query})
    data = response.json()

    return data

def convert_to_geojson(osm_data):
    features = []

    for node in osm_data['elements']:
        properties = {
            'name': node['tags'].get('name', ''),
            'address': node['tags'].get('addr:street', ''),
            'opening_hours': node['tags'].get('opening_hours', ''),
        }

        geometry = {
            'type': 'Point',
            'coordinates': [node['lon'], node['lat']]
        }

        features.append({
            'type': 'Feature',
            'properties': properties,
            'geometry': geometry
        })

    geojson = {
        'type': 'FeatureCollection',
        'features': features
    }

    return geojson

def save_to_geojson(geojson, file_name):
    with open(file_name, 'w') as f:
        f.write(json.dumps(geojson))

if __name__ == "__main__":
    # Set the coordinates for your location
    latitude,longitude = 28.765537, 77.483395 # Replace with your latitude # Replace with your longitude
    search_radius = 200  # Replace with your desired search radius in meters

    # Get OSM data
    osm_data = get_osm_data(latitude, longitude, search_radius)

    # Convert OSM data to GeoJSON format
    geojson_data = convert_to_geojson(osm_data)

    # Define the GeoJSON file name
    geojson_file_name = "cafe_data.geojson"  # Replace with your preferred file name

    # Save the GeoJSON data to a file
    save_to_geojson(geojson_data, geojson_file_name)

    print(f"Data saved to {geojson_file_name}")
