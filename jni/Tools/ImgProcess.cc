#include "ImgProcess.h"

CImg<float> CImgGrayScale(CImg<float> img) {
	CImg<float> grayscaled = img;
	cimg_forXY(img, x, y) {
		int r = img(x, y, 0);
		int g = img(x, y, 1);
		int b = img(x, y, 2);
		double newValue = (r * 0.2126 + g * 0.7152 + b * 0.0722);
		grayscaled(x, y, 0) = newValue;
		grayscaled(x, y, 1) = newValue;
		grayscaled(x, y, 2) = newValue;
	}
	return grayscaled;
}
