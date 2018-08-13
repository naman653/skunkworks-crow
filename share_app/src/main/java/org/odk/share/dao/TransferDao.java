package org.odk.share.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import org.odk.share.application.Share;
import org.odk.share.dto.TransferInstance;

import java.util.ArrayList;
import java.util.List;

import static org.odk.share.provider.TransferProvider.CONTENT_URI;

/**
 * Created by laksh on 6/28/2018.
 */

public class TransferDao {

    public Cursor getSentInstancesCursor() {
        String selection = TransferInstance.TRANSFER_STATUS + " =? ";
        String[] selectionArgs = {TransferInstance.STATUS_FORM_SENT};
        return getInstancesCursor(null, selection, selectionArgs, null);
    }

    public Cursor getReceiveInstancesCursor() {
        String selection = TransferInstance.TRANSFER_STATUS + " =? ";
        String[] selectionArgs = {TransferInstance.STATUS_FORM_RECEIVE};
        return getInstancesCursor(null, selection, selectionArgs, null);
    }

    public Cursor getReviewedInstancesCursor() {
        String selection = TransferInstance.REVIEW_STATUS + " IN (?,?) ";
        String[] selectionArgs = {String.valueOf(TransferInstance.STATUS_ACCEPTED),
                String.valueOf(TransferInstance.STATUS_REJECTED)};
        return getInstancesCursor(null, selection, selectionArgs, null);
    }

    public Cursor getUnreviewedInstancesCursor() {
        String selection = TransferInstance.REVIEW_STATUS + " =? ";
        String[] selectionArgs = {String.valueOf(TransferInstance.STATUS_UNREVIEWED)};
        return getInstancesCursor(null, selection, selectionArgs, null);
    }

    public CursorLoader getSentInstancesCursorLoader() {
        String selection = TransferInstance.TRANSFER_STATUS + " =? ";
        String[] selectionArgs = {TransferInstance.STATUS_FORM_SENT};

        return getInstancesCursorLoader(null, selection, selectionArgs, null);
    }

    public CursorLoader getReceiveInstancesCursorLoader() {
        String selection = TransferInstance.TRANSFER_STATUS + " =? ";
        String[] selectionArgs = {TransferInstance.STATUS_FORM_RECEIVE};

        return getInstancesCursorLoader(null, selection, selectionArgs, null);
    }

    public CursorLoader getReviewedInstancesCursorLoader() {
        String selection = TransferInstance.REVIEW_STATUS + " IN (?,?) ";
        String[] selectionArgs = {String.valueOf(TransferInstance.STATUS_ACCEPTED),
                String.valueOf(TransferInstance.STATUS_REJECTED)};

        return getInstancesCursorLoader(null, selection, selectionArgs, null);
    }

    public Cursor getInstancesCursor(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return Share.getInstance().getContentResolver()
                .query(CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    public Cursor getInstanceCursorFromId(long id) {
        String selection = TransferInstance.ID + " =? ";
        String[] selectionArgs = {String.valueOf(id)};
        return getInstancesCursor(null, selection, selectionArgs, null);
    }

    public CursorLoader getInstancesCursorLoader(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return new CursorLoader(Share.getInstance(), CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    public int updateInstance(ContentValues values, String where, String[] whereArgs) {
        return Share.getInstance().getContentResolver().update(CONTENT_URI, values, where, whereArgs);
    }


    public List<TransferInstance> getInstancesFromCursor(Cursor cursor) {
        List<TransferInstance> instances = new ArrayList<>();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int idColumnIndex = cursor.getColumnIndex(TransferInstance.ID);
                    int isReviewedColumnIndex = cursor.getColumnIndex(TransferInstance.REVIEW_STATUS);
                    int instructionColumnIndex = cursor.getColumnIndex(TransferInstance.INSTRUCTIONS);
                    int instanceIdColumnIndex = cursor.getColumnIndex(TransferInstance.INSTANCE_ID);
                    int transferStatusColumnIndex = cursor.getColumnIndex(TransferInstance.TRANSFER_STATUS);
                    int lastStatusChangeDateColumnIndex = cursor.getColumnIndex(TransferInstance.LAST_STATUS_CHANGE_DATE);

                    TransferInstance transferInstance = new TransferInstance();
                    transferInstance.setId(cursor.getLong(idColumnIndex));
                    transferInstance.setReviewed(cursor.getInt(isReviewedColumnIndex));
                    transferInstance.setInstructions(cursor.getString(instructionColumnIndex));
                    transferInstance.setInstanceId(cursor.getLong(instanceIdColumnIndex));
                    transferInstance.setTransferStatus(cursor.getString(transferStatusColumnIndex));
                    transferInstance.setLastStatusChangeDate(cursor.getLong(lastStatusChangeDateColumnIndex));

                    instances.add(transferInstance);
                }
            } finally {
                cursor.close();
            }
        }
        return instances;
    }
}