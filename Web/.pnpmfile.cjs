module.exports = {
  hooks: {
    readPackage(pkg) {
      if (pkg.dependencies && pkg.dependencies['@videojs/vhs-utils']) {
        pkg.dependencies['@videojs/vhs-utils'] = '^4.0.0';
      }
      return pkg;
    }
  }
};
