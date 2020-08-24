#include "com_ndk_test_OpenCVTest.h"

#include <vector>
#include <random>
#include <chrono>
#include <assert.h>

#include <opencv2/core/ocl.hpp>

#include <opencv2/core/utility.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/calib3d/calib3d.hpp>

#undef  LOG
#define LOG_TAG	"com_ndk_test_OpenCVTest"

#include "Tools/log.h"
#include "Tools/tic_toc.h"

static void test_opencv() {
    LOGI("[cggos ocl] %s: cv::ocl::haveOpenCL: %d\n", __FUNCTION__, cv::ocl::haveOpenCL());
    LOGI("[cggos ocl] %s: cv::ocl::useOpenCL: %d\n", __FUNCTION__, cv::ocl::useOpenCL());
    cv::ocl::setUseOpenCL(true);
    LOGI("[cggos ocl] %s: cv::ocl::useOpenCL: %d\n", __FUNCTION__, cv::ocl::useOpenCL());

    std::vector<cv::ocl::PlatformInfo> platforms;
    cv::ocl::getPlatfomsInfo(platforms);
    if(platforms.empty())
        LOGI("[cggos ocl] %s: platforms.empty\n", __FUNCTION__);
    for(auto plt : platforms) {
         LOGI("[cggos ocl] %s: deviceNumber: %d\n", __FUNCTION__, plt.deviceNumber());
         std::string str_name = plt.name();
         LOGI("[cggos ocl] %s: name: %s\n", __FUNCTION__, str_name.c_str());
    }

    cv::UMat uimg;
    cv::Mat img = cv::imread("/storage/emulated/0/cglog_larvio/test.png", cv::IMREAD_GRAYSCALE);
    std::vector<cv::Point2f> corners;

    for(int i=0; i<5; i++) {
        TicToc t_01;
        img.copyTo(uimg);
        LOGI("[cggos ocl] %s time 01:  %f ms\n", __FUNCTION__, t_01.toc());

        TicToc t_02;
        cv::goodFeaturesToTrack(uimg, corners, 100, 0.01, 20);
        LOGI("[cggos ocl] %s time 02:  %f ms\n", __FUNCTION__, t_02.toc());
    }
}


/*
 * Class:     com_ndk_test_OpenCVTest
 * Method:    test
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_ndk_test_OpenCVTest_test
  (JNIEnv *, jclass){

	test_opencv();
}