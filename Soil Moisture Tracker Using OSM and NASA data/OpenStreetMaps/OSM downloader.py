import requests
import geopandas as gpd

def get_osm_data(lat, lon, radius, data_format="json"):
    bbox = [lon - (radius / 111320), lat - (radius / 111320), lon + (radius / 111320), lat + (radius / 111320)]
    url = f"https://www.openstreetmap.org/api/0.6/{data_format}?bbox={bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]}"
    response = requests.get(url)

    if response.status_code == 200:
        return response.text
    else:
        raise Exception(f"Error {response.status_code} while fetching OSM data")

def main():
    # Coordinates for desired location (example: latitude: 26.141697, longitude: 85.365048 for Lumbini)
    lat, lon = 28.765537, 77.483395

    # Download GIS data in GeoJSON format
    osm_data = get_osm_data(lat, lon, 10)

    # Save data to a GeoJSON file
    with open("output.geojson", "w") as f:
        f.write(osm_data)

    # Load GeoJSON data into a GeoDataFrame
    gdf = gpd.read_file("output.geojson")

    # Save GeoDataFrame to a Shapefile
    gdf.to_file("output.shp")

if __name__ == "__main__":
    main()