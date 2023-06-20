let appContext = {
  ffmpegPath: null,
  dataDir: null
}

function getFFMpegPath() {
  return appContext.ffmpegPath;
}

function getDataDir() {
  return appContext.dataDir;
}

function setFFMpegPath(path) {
  appContext.ffmpegPath = path;
}

function setDataDir(dir) {
  appContext.dataDir = dir;
}

exports.getFFMpegPath = getFFMpegPath;
exports.getDataDir = getDataDir;
exports.setFFMpegPath = setFFMpegPath;
exports.setDataDir = setDataDir;