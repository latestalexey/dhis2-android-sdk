package org.hisp.dhis.android.core.category;


import static junit.framework.Assert.assertTrue;

import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.google.common.truth.Truth;

import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.common.D2Factory;
import org.hisp.dhis.android.core.common.Payload;
import org.hisp.dhis.android.core.data.api.ResponseValidator;
import org.hisp.dhis.android.core.data.database.AbsStoreTestCase;
import org.hisp.dhis.android.core.data.server.RealServerMother;
import org.hisp.dhis.android.core.resource.ResourceHandler;
import org.hisp.dhis.android.core.resource.ResourceStore;
import org.hisp.dhis.android.core.resource.ResourceStoreImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import retrofit2.Response;


public class CategoryComboCallEndpointRealShould extends AbsStoreTestCase {

    private D2 d2;

    @Override
    @Before
    public void setUp() throws IOException {
        super.setUp();
        d2 = D2Factory.create(RealServerMother.url, databaseAdapter());
    }

    @Test
    public void download_category_combos() throws Exception {

        Response responseLogIn = d2.logIn(RealServerMother.user, RealServerMother.password).call();
        Truth.assertThat(responseLogIn.isSuccessful()).isTrue();

        downloadCategories();

        assertNotCombosInDB();
        assertNotCategoryCombosLinkInDB();

        CategoryComboCallEndpoint comboCallEndpoint = provideComboCallEndpoint();
        Response<Payload<CategoryCombo>> responseCategory = comboCallEndpoint.call();

        assertParseData(responseCategory);

        assertRelations();
    }

    private void assertParseData(Response<Payload<CategoryCombo>> responseCategory) {
        assertTrue(responseCategory.isSuccessful());
        assertTrue(hasCombos(responseCategory));
    }

    private void assertRelations() {
        assertThereAreCombosInDB();
        assertThereAreCategoryCombosLinkInDB();
        assertThereAreCategoryOptionCombosInDB();
    }

    private void downloadCategories() throws Exception {
        CategoryCallEndpoint categoryCallEndpoint = provideCategoryCallEndpoint();
        categoryCallEndpoint.call();

    }

    @NonNull
    private CategoryCallEndpoint provideCategoryCallEndpoint() {
        CategoryQuery query = CategoryQuery.defaultQuery();

        CategoryService categoryService = d2.retrofit().create(CategoryService.class);

        ResponseValidator<Category> validator = new ResponseValidator<>();

        CategoryStore store = new CategoryStoreImpl(databaseAdapter());

        CategoryOptionStore categoryOptionStore = new CategoryOptionStoreImpl(databaseAdapter());

        CategoryOptionHandler categoryOptionHandler = new CategoryOptionHandler(
                categoryOptionStore);

        CategoryOptionLinkStore categoryOptionLinkStore = new CategoryOptionLinkStoreImpl(
                databaseAdapter());

        CategoryHandler handler = new CategoryHandler(store, categoryOptionHandler,
                categoryOptionLinkStore);
        ResourceStore resourceStore = new ResourceStoreImpl(databaseAdapter());
        ResourceHandler resourceHandler = new ResourceHandler(resourceStore);
        Date serverDate = new Date();

        return new CategoryCallEndpoint(query, categoryService, validator, handler, resourceHandler,
                databaseAdapter(), serverDate);

    }

    private void assertNotCombosInDB() {
        Cursor combos = selectAllCombosFromDB();
        assertTrue(combos.getCount() == 0);
    }

    private void assertNotCategoryCombosLinkInDB() {
        Cursor combos = selectAllCategoryCombosLinksFromDB();
        assertTrue(combos.getCount() == 0);
    }

    private void assertThereAreCombosInDB() {
        Cursor combos = selectAllCombosFromDB();
        assertTrue(combos.getCount() > 0);
    }

    private void assertThereAreCategoryCombosLinkInDB() {
        Cursor combos = selectAllCategoryCombosLinksFromDB();
        assertTrue(combos.getCount() > 0);
    }

    private void assertThereAreCategoryOptionCombosInDB() {
        Cursor combos = selectAllOptionCombosFromDB();
        assertTrue(combos.getCount() > 0);
    }

    private Cursor selectAllCombosFromDB() {
        String sqlQuery = SQLiteQueryBuilder.buildQueryString(false, CategoryComboModel.TABLE,
                CategoryComboModel.PROJECTION, null,
                null, null, null, null);


        return databaseAdapter().query(sqlQuery);
    }

    private Cursor selectAllCategoryCombosLinksFromDB() {
        String sqlQuery = SQLiteQueryBuilder.buildQueryString(false, CategoryComboLinkModel.TABLE,
                CategoryComboLinkModel.PROJECTION, null,
                null, null, null, null);


        return databaseAdapter().query(sqlQuery);
    }

    private Cursor selectAllOptionCombosFromDB() {
        String sqlQuery = SQLiteQueryBuilder.buildQueryString(false, CategoryOptionComboModel.TABLE,
                CategoryOptionComboModel.PROJECTION, null,
                null, null, null, null);


        return databaseAdapter().query(sqlQuery);
    }


    @NonNull
    private CategoryComboCallEndpoint provideComboCallEndpoint() {
        CategoryComboQuery query = CategoryComboQuery.defaultQuery();

        CategoryComboService comboService = d2.retrofit().create(CategoryComboService.class);
        CategoryComboLinkStore categoryComboLinkStore = new CategoryComboLinkStoreImpl(
                databaseAdapter());

        CategoryOptionComboStore optionComboStore = new CategoryOptionComboStoreImpl(
                databaseAdapter());
        CategoryOptionComboHandler optionComboHandler = new CategoryOptionComboHandler(
                optionComboStore);

        ResponseValidator<CategoryCombo> validator = new ResponseValidator<>();

        CategoryComboStore store = new CategoryComboStoreImpl(databaseAdapter());

        CategoryOptionComboLinkCategoryStore
                categoryComboOptionLinkCategoryStore = new CategoryOptionComboLinkCategoryStoreImpl(
                databaseAdapter());

        CategoryComboHandler handler = new CategoryComboHandler(store,
                categoryComboOptionLinkCategoryStore, categoryComboLinkStore,
                optionComboHandler);

        ResourceStore resourceStore = new ResourceStoreImpl(databaseAdapter());
        ResourceHandler resourceHandler = new ResourceHandler(resourceStore);
        Date serverDate = new Date();

        return new CategoryComboCallEndpoint(query, comboService, validator, handler,
                resourceHandler,
                databaseAdapter(), serverDate);

    }

    private boolean hasCombos(Response<Payload<CategoryCombo>> response) {
        return !response.body().items().isEmpty();
    }
}
