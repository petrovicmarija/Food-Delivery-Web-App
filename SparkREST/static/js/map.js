const map = new ol.Map({ target: "map" });

map.setView(
  new ol.View({
    center: ol.proj.fromLonLat([19.7245,45.2889]),
    zoom: 12
  })
);

const apiKey = "AAPK8cad6fdb843d461b83eff8dfd08b20dcBLf02R4yV5jXllwflSmWOdjsqUK-9v-etIhDJdP7et4xmJgOD9xBn2FDg-DOwbvs";

const basemapId = "ArcGIS:Navigation";

const basemapURL = "https://basemaps-api.arcgis.com/arcgis/rest/services/styles/" + basemapId + "?type=style&token=" + apiKey;

olms(map, basemapURL);

      const popup = new Popup();
map.addOverlay(popup);

map.on("click", (e) => {
  const coords = ol.proj.transform(e.coordinate, "EPSG:3857", "EPSG:4326");

  const authentication = new arcgisRest.ApiKey({
    key: apiKey
  });

  arcgisRest
    .reverseGeocode(coords, { authentication })
    .then((result) => {
      const message = `${result.address.LongLabel}<br>` + `${result.location.x.toLocaleString()}, ${result.location.y.toLocaleString()}`;

    popup.show(e.coordinate, message);

  })

  .catch((error) => {
    popup.hide();
    console.error(error);
  })
});
