// node의 path 모듈을 가져옵니다.
const path = require('path');

module.exports = {
  // 웹팩 설정
  configureWebpack: {
    // resolve.alias는 모듈을 더 쉽게 import, require 하게 만듭니다.
    resolve: {
      alias: {
        // '@'는 현재 프로젝트의 client/src/까지의 최종 경로를 의미합니다.
        '@': path.join(__dirname, 'src/'),
      },
    },
  },
};
