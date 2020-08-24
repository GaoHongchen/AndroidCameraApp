#include "com_ndk_test_FFTW3.h"

#include <stdlib.h>
#include <stdio.h>

#include <fftw3.h>

#include <CImg.h>
using namespace cimg_library;

#undef  LOG
#define LOG_TAG	"com_ndk_test_FFTW3"

#include "Tools/log.h"
#include "Tools/types.h"

/*
 * Class:     com_ndk_test_FFTW3
 * Method:    DFT2DfromPath
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_ndk_test_FFTW3_DFT2DfromPath(JNIEnv *pEnv, jclass arg, jstring imgPath)
{
	char *imgDir = jstring2pchar(pEnv,imgPath);
	std::string strImgDir = (std::string)imgDir+"/";
	std::string strImgNameIn  = "lena.bmp";
	std::string strImgNameOut = "lenaFFTW.bmp";
	std::string strImgIn  = strImgDir + strImgNameIn;
	std::string strImgOut = strImgDir + strImgNameOut;

	LOGD("Java_com_ndk_fftw_FFTW3_DFT2DfromPath: strImgIn = %s",strImgIn.c_str());

	CImg<float> imgIn(strImgIn.c_str());
	int N,Nx,Ny;
	N=imgIn.size();
	Nx=imgIn.width();
	Ny=imgIn.height();
	CImg<float> imgOut(Nx,Ny,1,3,0);// ���������

	fftwf_complex *in  = (fftwf_complex*)fftwf_malloc(Ny*Nx*sizeof(fftwf_complex));
	fftwf_complex *out = (fftwf_complex*)fftwf_malloc(Ny*Nx*sizeof(fftwf_complex));

	fftwf_plan planDFT2D_F = fftwf_plan_dft_2d(Nx,Ny, in, out,FFTW_FORWARD,FFTW_ESTIMATE);

	int v=0;
	while(v<3)
	{
		for(int x=0;x<Nx;x++)
		{
			for(int y=0;y<Ny;y++)
			{
				in[y+x*Ny][0]=imgIn(x,y,0,v);
			}
		}

		fftwf_execute(planDFT2D_F);

		for(int x=0;x<Nx;x++)
		{
			for(int y=0;y<Ny;y++)
			{
				*(imgOut.data(x,y,v))=sqrt(pow(out[y+x*Ny][0],2)+pow(out[y+x*Ny][1],2));
				if((*(imgOut.data(x,y,v)))>25000)
				{
					*(imgOut.data(x,y,v))=25000;
				}
			}
		}
		v++;
	}

	fftwf_destroy_plan(planDFT2D_F);
	fftwf_cleanup();
	if (in != NULL)
	{
		fftwf_free(in);
	}
	if (out != NULL)
	{
		fftwf_free(out);
	}

	CImg<float> imageOut(imgOut.data(),Nx,Ny,1,3);
	imageOut.normalize(0,255);
	imageOut.save_bmp(strImgOut.c_str());

	return;
}

/*
 * Class:     com_ndk_test_FFTW3
 * Method:    DFT2DfromAddr
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ndk_test_FFTW3_DFT2DfromAddr(JNIEnv *pEnv, jclass arg, jlong imgAddr)
{

}
