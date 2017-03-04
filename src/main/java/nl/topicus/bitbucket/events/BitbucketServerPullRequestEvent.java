/*
 * The MIT License
 *
 * Copyright (c) 2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nl.topicus.bitbucket.events;

import nl.topicus.bitbucket.model.pullrequest.BitbucketServerPullRequest;
import nl.topicus.bitbucket.model.repository.BitbucketServerRepository;
import nl.topicus.bitbucket.model.repository.BitbucketServerRepositoryOwner;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BitbucketServerPullRequestEvent
{
    private BitbucketServerRepositoryOwner actor;

    private BitbucketServerPullRequest pullrequest;

    private BitbucketServerRepository repository;

    public BitbucketServerRepositoryOwner getActor()
    {
        return actor;
    }

    public void setActor(BitbucketServerRepositoryOwner actor)
    {
        this.actor = actor;
    }

    public BitbucketServerPullRequest getPullrequest()
    {
        return pullrequest;
    }

    public void setPullrequest(BitbucketServerPullRequest pullrequest)
    {
        this.pullrequest = pullrequest;
    }

    public BitbucketServerRepository getRepository()
    {
        return repository;
    }

    public void setRepository(BitbucketServerRepository repository)
    {
        this.repository = repository;
    }

}
