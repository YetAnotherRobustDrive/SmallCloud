let appContext = {
  ffmpegPath: null,
  dataDir: null,
  aescryptPath: null
}

function getFFMpegPath() {
  return appContext.ffmpegPath;
}

function getDataDir() {
  return appContext.dataDir;
}

function getAEScryptPath() {
  return appContext.aescryptPath;
}

function setFFMpegPath(path) {
  appContext.ffmpegPath = path;
}

function setDataDir(dir) {
  appContext.dataDir = dir;
}

 function setAEScryptPath(path) {
  appContext.aescryptPath = path;
}

exports.getFFMpegPath = getFFMpegPath;
exports.getDataDir = getDataDir;
exports.setFFMpegPath = setFFMpegPath;
exports.setDataDir = setDataDir;
exports.getAEScryptPath = getAEScryptPath;
exports.setAEScryptPath = setAEScryptPath;