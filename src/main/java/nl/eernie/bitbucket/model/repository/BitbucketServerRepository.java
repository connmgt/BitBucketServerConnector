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
package nl.eernie.bitbucket.model.repository;

public class BitbucketServerRepository
{
	private String scmId;

	private BitbucketServerProject project;

	private String slug;

	// JSON mapping added in setter because the field can not be called "public"
	private Boolean publc;

	public String getScmId()
	{
		return scmId;
	}

	public void setScmId(String scmId)
	{
		this.scmId = scmId;
	}

	public String getFullName()
	{
		return project.getKey() + "/" + slug;
	}

	public BitbucketServerRepositoryOwner getOwner()
	{
		return new BitbucketServerRepositoryOwner(project.getKey(), project.getKey());
	}

	public String getOwnerName()
	{
		return project.getKey();
	}

	public String getSlug()
	{
		return slug;
	}

	public void setSlug(String slug)
	{
		this.slug = slug;
	}

	public BitbucketServerProject getProject()
	{
		return project;
	}

	public void setProject(BitbucketServerProject p)
	{
		this.project = p;
	}

	public boolean isPublic()
	{
		return publc;
	}

	public void setPublic(Boolean publc)
	{
		this.publc = publc;
	}
}
