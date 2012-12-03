package com.cts.ms.higheredu.imagecaptcha.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cts.ms.higheredu.imagecaptcha.dao.CategoryImages;
import com.cts.ms.higheredu.imagecaptcha.dao.ImageCategory;
import com.cts.ms.higheredu.imagecaptcha.util.CaptchaResponse;
import com.cts.ms.higheredu.imagecaptcha.util.CaptchaUtility;

public class CaptchaCache {

	private static CaptchaCache s_cacheInstance = null;
	private List<Integer> m_categoryIdList = new ArrayList<Integer>();
	private Map<Integer, ImageCategory> m_catIdToImageCatMap = new HashMap<Integer, ImageCategory>();
	private Map<Integer, List<CategoryImages>> m_catIdToImagesMap = new HashMap<Integer, List<CategoryImages>>();
	private Map<String, CaptchaResponse> m_tokenToSourceImgsMap = new HashMap<String, CaptchaResponse>();

	public static CaptchaCache getInstance() {
		if (s_cacheInstance == null) {
			s_cacheInstance = new CaptchaCache();
		}
		return s_cacheInstance;
	}

	private CaptchaCache() {
		loadCategoryImagesData();
	}

	private void loadCategoryImagesData() {
		List<ImageCategory> categories = CaptchaUtility.loadCategories();
		for (ImageCategory imageCat : categories) {
			m_catIdToImageCatMap.put(imageCat.getCategoryId(), imageCat);
			m_categoryIdList.add(imageCat.getCategoryId());
		}

		List<CategoryImages> categoryImages = CaptchaUtility
				.loadCategoryImages();
		for (CategoryImages catImage : categoryImages) {
			List<CategoryImages> catImageList = m_catIdToImagesMap.get(catImage
					.getCategoryId());
			if (catImageList != null) {
				catImageList.add(catImage);
			} else {
				catImageList = new ArrayList<CategoryImages>();
				catImageList.add(catImage);
				m_catIdToImagesMap.put(catImage.getCategoryId(), catImageList);
			}
		}
	}

	public ImageCategory getTargetImageCategory() {
		int catId = m_categoryIdList.get((int) (m_categoryIdList.size() * Math
				.random()));
		return m_catIdToImageCatMap.get(catId);
	}

	public ImageCategory getNonTargetImageCat(int targetCatId) {
		int catId = m_categoryIdList.get((int) (m_categoryIdList.size() * Math
				.random()));
		if (catId != targetCatId) {
			return m_catIdToImageCatMap.get(catId);
		} else {
			return getNonTargetImageCat(targetCatId);
		}
	}

	public List<CategoryImages> getImages(int noOfImages, int categoryId) {
		List<CategoryImages> images = m_catIdToImagesMap.get(categoryId);
		List<CategoryImages> catImages = new ArrayList<CategoryImages>();
		int max = images.size();
		while (catImages.size() < noOfImages) {
			int random = (int) (max * Math.random());
			if (!catImages.contains(images.get(random)))
				catImages.add(images.get(random));
		}
		return catImages;
	}

	public Map<String, CaptchaResponse> getTokenImagesMap() {
		return m_tokenToSourceImgsMap;
	}
}
