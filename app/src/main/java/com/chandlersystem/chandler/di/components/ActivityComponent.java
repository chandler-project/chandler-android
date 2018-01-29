package com.chandlersystem.chandler.di.components;

import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.di.scopes.PerActivity;
import com.chandlersystem.chandler.ui.become_shipper.BecomeShipperActivity;
import com.chandlersystem.chandler.ui.cart.CartActivity;
import com.chandlersystem.chandler.ui.create_deal.CreateDealActivity;
import com.chandlersystem.chandler.ui.create_request.CreateRequestActivity;
import com.chandlersystem.chandler.ui.create_request.SelectCategoryFragment;
import com.chandlersystem.chandler.ui.deal.CategoryDetailActivity;
import com.chandlersystem.chandler.ui.deal.DealFragment;
import com.chandlersystem.chandler.ui.deal_detail.DealCommentFragment;
import com.chandlersystem.chandler.ui.deal_detail.DealDetailActivity;
import com.chandlersystem.chandler.ui.deal_detail.DealInfoFragment;
import com.chandlersystem.chandler.ui.feedback.FeedbackActivity;
import com.chandlersystem.chandler.ui.login.LoginActivity;
import com.chandlersystem.chandler.ui.main.MainActivity;
import com.chandlersystem.chandler.ui.profile.EditProfileActivity;
import com.chandlersystem.chandler.ui.profile.ProfileFragment;
import com.chandlersystem.chandler.ui.profile.UserProfileActivity;
import com.chandlersystem.chandler.ui.request_detail.BidFragment;
import com.chandlersystem.chandler.ui.request_detail.RequestDetailActivity;
import com.chandlersystem.chandler.ui.requests.RequestsFragment;
import com.chandlersystem.chandler.ui.select_category.SelectCategoryActivity;
import com.chandlersystem.chandler.ui.user_deal.UserDealActivity;
import com.chandlersystem.chandler.ui.user_request.UserRequestActivity;

import dagger.Component;

/**
 * Inject in every components what use module of both Application and Activity Module
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(CartActivity mainActivity);

    void inject(BidFragment bidFragment);

    void inject(UserProfileActivity userProfileActivity);

    void inject(LoginActivity loginActivity);

    void inject(RequestDetailActivity requestDetailActivity);

    void inject(DealDetailActivity dealDetailActivity);

    void inject(FeedbackActivity feedbackActivity);

    void inject(CategoryDetailActivity categoryDetailActivity);

    void inject(DealCommentFragment dealCommentFragment);

    void inject(RequestsFragment requestsFragment);

    void inject(DealFragment dealFragment);

    void inject(SelectCategoryFragment selectCategoryFragment);

    void inject(DealInfoFragment dealInfoFragment);

    void inject(SelectCategoryActivity selectCategoryActivity);

    void inject(ProfileFragment profileFragment);

    void inject(CreateDealActivity createDealActivity);

    void inject(CreateRequestActivity createRequestActivity);

    void inject(EditProfileActivity editProfileActivity);

    void inject(BecomeShipperActivity becomeShipperActivity);

    void inject(UserDealActivity userDealActivity);

    void inject(UserRequestActivity userRequestActivity);
}
