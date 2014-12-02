/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.asset.entry.set.service.impl;

import com.liferay.asset.entry.set.model.AssetEntrySet;
import com.liferay.asset.entry.set.service.base.AssetEntrySetLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;

import java.util.Date;
import java.util.List;

/**
 * @author Calvin Keum
 */
public class AssetEntrySetLocalServiceImpl
	extends AssetEntrySetLocalServiceBaseImpl {

	public AssetEntrySet addAssetEntrySet(
			long userId, long parentAssetEntrySetId, long creatorClassNameId,
			long creatorClassPK, String payload, int type,
			int socialRelationType, ServiceContext serviceContext)
		throws PortalException, SystemException {

		User user = userPersistence.findByPrimaryKey(userId);

		Date now = new Date();

		long assetEntrySetId = counterLocalService.increment();

		AssetEntrySet assetEntrySet = assetEntrySetPersistence.create(
			assetEntrySetId);

		assetEntrySet.setCompanyId(user.getCompanyId());
		assetEntrySet.setUserId(user.getUserId());
		assetEntrySet.setUserName(user.getFullName());
		assetEntrySet.setCreateTime(now.getTime());
		assetEntrySet.setModifiedTime(now.getTime());
		assetEntrySet.setParentAssetEntrySetId(parentAssetEntrySetId);
		assetEntrySet.setCreatorClassNameId(creatorClassNameId);
		assetEntrySet.setCreatorClassPK(creatorClassPK);
		assetEntrySet.setPayload(payload);

		assetEntrySetPersistence.update(assetEntrySet);

		updateAsset(
			assetEntrySet, serviceContext.getAssetCategoryIds(),
			serviceContext.getAssetTagNames());

		return assetEntrySet;
	}

	@Override
	public AssetEntrySet deleteAssetEntrySet(AssetEntrySet assetEntrySet)
		throws PortalException, SystemException {

		assetEntrySetPersistence.remove(assetEntrySet);

		AssetEntryLocalServiceUtil.deleteEntry(
			AssetEntrySet.class.getName(), assetEntrySet.getAssetEntrySetId());

		return assetEntrySet;
	}

	@Override
	public AssetEntrySet deleteAssetEntrySet(long assetEntrySetId)
		throws PortalException, SystemException {

		AssetEntrySet assetEntrySet = assetEntrySetPersistence.findByPrimaryKey(
			assetEntrySetId);

		return deleteAssetEntrySet(assetEntrySet);
	}

	@Override
	public AssetEntrySet getAssetEntrySet(long assetEntrySetId)
		throws PortalException, SystemException {

		return assetEntrySetPersistence.findByPrimaryKey(assetEntrySetId);
	}

	public AssetEntrySet getAssetEntrySet(
			long parentAssetEntrySetId, long creatorClassNameId,
			long creatorClassPK, int start, int end)
		throws PortalException, SystemException {

		return assetEntrySetPersistence.findByPAESI_CCNI_CCPK(
			parentAssetEntrySetId, creatorClassNameId, creatorClassPK);
	}

	public List<AssetEntrySet> getAssetEntrySets(
			long parentAssetEntrySetId, long creatorClassNameId, int type,
			int start, int end, OrderByComparator obc)
		throws SystemException {

		return assetEntrySetPersistence.findByPAESI_CCNI(
			parentAssetEntrySetId, creatorClassNameId, start, end, obc);
	}

	public List<AssetEntrySet> getAssetEntrySets(
			long creatorClassNameId, long creatorClassPK, String assetTagName,
			boolean andOperator, int start, int end)
		throws SystemException {

		return assetEntrySetFinder.findByCCNI_CCPK_ATN(
			creatorClassNameId, creatorClassPK, assetTagName, andOperator,
			start, end);
	}

	public List<AssetEntrySet> getAssetEntrySets(
			long creatorClassNameId, String assetTagName, int start, int end)
		throws SystemException {

		return assetEntrySetFinder.findByCCNI_ATN(
			creatorClassNameId, assetTagName, start, end);
	}

	public int getAssetEntrySetsCount(
			long parentAssetEntrySetId, long creatorClassNameId)
		throws SystemException {

		return assetEntrySetPersistence.countByPAESI_CCNI(
			parentAssetEntrySetId, creatorClassNameId);
	}

	public int getAssetEntrySetsCount(
			long creatorClassNameId, long creatorClassPK,
			long parentAssetEntrySetId)
		throws SystemException {

		return assetEntrySetPersistence.countByPAESI_CCNI_CCPK(
			parentAssetEntrySetId, creatorClassNameId, creatorClassPK);
	}

	public int getAssetEntrySetsCount(
			long creatorClassNameId, long creatorClassPK, String assetTagName,
			boolean andOperator)
		throws SystemException {

		return assetEntrySetFinder.countByCCNI_CCPK_ATN(
			creatorClassNameId, creatorClassPK, assetTagName, andOperator);
	}

	public int getAssetEntrySetsCount(
			long creatorClassNameId, String assetTagName)
		throws SystemException {

		return assetEntrySetFinder.countByCCNI_ATN(
			creatorClassNameId, assetTagName);
	}

	public List<AssetEntrySet> getParentAssetEntrySetAssetEntrySets(
			long parentAssetEntrySetId, int start, int end,
			OrderByComparator orderByComparator)
		throws SystemException {

		return assetEntrySetPersistence.findByParentAssetEntrySetId(
			parentAssetEntrySetId, start, end, orderByComparator);
	}

	public int getParentAssetEntrySetAssetEntrySetsCount(
			long parentAssetEntrySetId)
		throws SystemException {

		return assetEntrySetPersistence.countByParentAssetEntrySetId(
			parentAssetEntrySetId);
	}

	public void updateAsset(
			AssetEntrySet assetEntrySet, long[] assetCategoryIds,
			String[] assetTagNames)
		throws PortalException, SystemException {

		Group group = GroupLocalServiceUtil.getCompanyGroup(
			assetEntrySet.getCompanyId());

		AssetEntryLocalServiceUtil.updateEntry(
			assetEntrySet.getUserId(), group.getGroupId(),
			AssetEntrySet.class.getName(), assetEntrySet.getAssetEntrySetId(),
			assetCategoryIds, assetTagNames);
	}

	public AssetEntrySet updateAssetEntrySet(
			long assetEntrySetId, String payload, ServiceContext serviceContext)
		throws PortalException, SystemException {

		AssetEntrySet assetEntrySet = assetEntrySetPersistence.findByPrimaryKey(
			assetEntrySetId);

		Date now = new Date();

		assetEntrySet.setModifiedTime(now.getTime());
		assetEntrySet.setPayload(payload);

		assetEntrySetPersistence.update(assetEntrySet);

		updateAsset(
			assetEntrySet, serviceContext.getAssetCategoryIds(),
			serviceContext.getAssetTagNames());

		return assetEntrySet;
	}

}