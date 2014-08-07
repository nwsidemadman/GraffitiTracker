function dateFormat_yyyymmdd( oObj ) {
  date = new Date(oObj);
  yyyy = date.getFullYear();
  mm = ((date.getMonth() + 1) <= 9 ? ('0' + (date.getMonth() + 1)) : (date.getMonth() + 1));
  dd = (date.getDate() <= 9 ? ('0' + date.getDate()) : date.getDate());
  return yyyy + '-' + mm + '-' + dd;
}