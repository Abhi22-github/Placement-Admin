package com.roaaserver.placementadmin.Other;

import com.roaaserver.placementadmin.Models.AdminDetailsClass;

public interface FirestoreCallback {
    void markVerified(AdminDetailsClass adminDetailsClass);
    void markUnverified(AdminDetailsClass adminDetailsClass);

}
