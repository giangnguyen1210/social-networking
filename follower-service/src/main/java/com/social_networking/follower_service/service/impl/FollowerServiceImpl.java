package com.social_networking.follower_service.service.impl;

import com.social_networking.follower_service.dto.request.FollowerRequest;
import com.social_networking.follower_service.model.Follower;
import com.social_networking.follower_service.reducer.FollowerReducer;
import com.social_networking.follower_service.repository.FollowerRepository;
import com.social_networking.follower_service.service.FollowerService;
import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.shared_service.dto.response.FollowerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FollowerServiceImpl implements FollowerService {
    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private FollowerReducer followerReducer;
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowerServiceImpl.class);


    @Override
    public BaseResponse saveFollow(FollowerRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        if(request.getFollowerId()!=null && request.getFollowingId()!=null && !request.getFollowerId().equals(request.getFollowingId())){
            Follower isFollowExist = followerRepository.findByFollowerIdAndFollowingIdAndIsFollowingTrue(request.getFollowerId(), request.getFollowingId());

            if(isFollowExist!=null){
                baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
                baseResponse.setErrorDesc("Follow exist");
            }else{
                Follower isFollowExistAndUnfollow = followerRepository.findByFollowerIdAndFollowingIdAndIsFollowingFalse(request.getFollowerId(), request.getFollowingId());
                if(isFollowExistAndUnfollow!=null){
                    isFollowExistAndUnfollow.setIsFollowing(true);
                    followerRepository.save(isFollowExistAndUnfollow);
                    FollowerResponse followerResponse = new FollowerResponse();
                    followerResponse.setFollowerId(isFollowExistAndUnfollow.getFollowerId());
                    followerResponse.setFollowingId(isFollowExistAndUnfollow.getFollowingId());
                    followerResponse.setIsFollowing(isFollowExistAndUnfollow.getIsFollowing());
                    followerReducer.sendFollower(followerResponse);
                    baseResponse.setData(isFollowExistAndUnfollow);
                }else{
                    Follower follower = new Follower();
                    follower.setFollowerId(request.getFollowerId());
                    follower.setFollowingId(request.getFollowingId());
                    followerRepository.save(follower);
                    FollowerResponse followerResponse = new FollowerResponse();
                    followerResponse.setFollowerId(follower.getFollowerId());
                    followerResponse.setFollowingId(follower.getFollowingId());
                    followerResponse.setIsFollowing(follower.getIsFollowing());
                    followerReducer.sendFollower(followerResponse);
                    baseResponse.setData(follower);
                }
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("Follow success");
            }
        }else{
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("FollowerId/ FollowingId not found or Not allow follow yourself");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse unFollow(FollowerRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        if(request.getFollowerId()!=null && request.getFollowingId()!=null && !request.getFollowerId().equals(request.getFollowingId())){
            Follower isFollowExist = followerRepository.findByFollowerIdAndFollowingIdAndIsFollowingTrue(request.getFollowerId(), request.getFollowingId());

            if(isFollowExist==null){
                baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
                baseResponse.setErrorDesc("Relation not exist");
            }else{
                isFollowExist.setIsFollowing(false);
                followerRepository.save(isFollowExist);
                FollowerResponse followerResponse = new FollowerResponse();
                followerResponse.setIsFollowing(isFollowExist.getIsFollowing());
                followerResponse.setFollowerId(isFollowExist.getFollowerId());
                followerResponse.setFollowingId(isFollowExist.getFollowingId());
                followerReducer.sendUnFollow(followerResponse);
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("Unfollow thành công");
                baseResponse.setData(followerResponse);

            }
        }else{
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("FollowerId/ FollowingId not found or Not un allow follow yourself");
        }
        return baseResponse;
    }



}
